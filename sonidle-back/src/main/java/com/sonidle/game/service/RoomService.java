package com.sonidle.game.service;

import com.sonidle.game.dto.SocketRoomDTO;
import com.sonidle.game.model.Player;
import com.sonidle.game.model.Room;
import com.sonidle.game.model.RoomSettings;
import com.sonidle.game.payload.CreateRoomPayload;
import com.sonidle.game.payload.JoinRoomPayload;
import com.sonidle.game.repository.PlayerRepository;
import com.sonidle.game.repository.RoomRepository;
import com.sonidle.game.repository.RoomSettingsRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomSettingsRepository roomSettingsRepository;
    private final PlayerRepository playerRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public RoomService(RoomRepository roomRepository, RoomSettingsRepository roomSettingsRepository, PlayerRepository playerRepository) {
        this.roomRepository = roomRepository;
        this.roomSettingsRepository = roomSettingsRepository;
        this.playerRepository = playerRepository;
    }

    public Room getRoom(UUID id) throws NotFoundException {
        return roomRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public SocketRoomDTO create(CreateRoomPayload payload) {
        Room room = new Room();
        room.setId(UUIDService.generate(roomRepository));

        RoomSettings roomSettings = new RoomSettings();
        roomSettings.setId(UUIDService.generate(roomSettingsRepository));
        roomSettings.setNbPlayersMax(payload.getNbPlayersMax());
        roomSettings.setRoundDuration(payload.getRoundDuration());
        room.setSettings(roomSettings);

        Player owner = new Player();
        owner.setId(UUIDService.generate(playerRepository));
        owner.setName(payload.getPlayerName());
        owner.setOwner(true);
        room.setPlayersIds(Collections.singletonList(owner.getId()));

        playerRepository.save(owner);
        roomSettingsRepository.save(roomSettings);
        roomRepository.save(room);

        SocketRoomDTO socketRoomDTO = SocketRoomDTO.toDTO(room, List.of(owner), List.of());

        createSocket(socketRoomDTO);

        return socketRoomDTO;
    }

    public SocketRoomDTO join(JoinRoomPayload payload) throws NotFoundException, BadRequestException {
        Room room = getRoom(payload.getRoomId());

        if (room.getSettings().getNbPlayersMax() <= room.getPlayersIds().size()) {
            throw new BadRequestException("The room is full");
        }
            Player player = new Player();
            player.setId(UUIDService.generate(playerRepository));
            player.setName(payload.getPlayerName());
            player.setOwner(false);
            playerRepository.save(player);
            room.getPlayersIds().add(player.getId());
            roomRepository.save(room);

            Iterable<Player> playersList = playerRepository.findAllById(room.getPlayersIds());
            List<Player> players = StreamSupport.stream(playersList.spliterator(), false).collect(Collectors.toList());

            SocketRoomDTO roomDTO = SocketRoomDTO.toDTO(room, players, List.of());
            createSocket(roomDTO);

            return roomDTO;
    }

    private void createSocket(SocketRoomDTO room) {
        messagingTemplate.convertAndSend("/room/" + room.getId(), room);
    }
}
