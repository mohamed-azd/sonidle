package com.sonidle.game.service;

import com.sonidle.game.dto.GuessDTO;
import com.sonidle.game.dto.SocketRoomDTO;
import com.sonidle.game.model.Music;
import com.sonidle.game.model.Player;
import com.sonidle.game.model.Room;
import com.sonidle.game.model.RoomSettings;
import com.sonidle.game.payload.CreateRoomPayload;
import com.sonidle.game.payload.GuessPayload;
import com.sonidle.game.payload.JoinRoomPayload;
import com.sonidle.game.payload.UpdateGenresPayload;
import com.sonidle.game.repository.MusicRepository;
import com.sonidle.game.repository.PlayerRepository;
import com.sonidle.game.repository.RoomRepository;
import com.sonidle.game.repository.RoomSettingsRepository;
import com.sonidle.game.service.interfaces.MusicPlatformService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

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
    private final MusicPlatformService musicPlatformService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MusicRepository musicRepository;

    public RoomService(RoomRepository roomRepository, RoomSettingsRepository roomSettingsRepository, PlayerRepository playerRepository, MusicPlatformService musicPlatformService) {
        this.roomRepository = roomRepository;
        this.roomSettingsRepository = roomSettingsRepository;
        this.playerRepository = playerRepository;
        this.musicPlatformService = musicPlatformService;
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

        publishRoomSocket(socketRoomDTO);

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

        SocketRoomDTO roomDTO = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), List.of());
        publishRoomSocket(roomDTO);

        return roomDTO;
    }

    public void updateGenres(UpdateGenresPayload payload) throws NotFoundException {
        Room room = getRoom(payload.getRoomId());
        RoomSettings settings = room.getSettings();
        settings.setGenres(payload.getGenres());
        roomRepository.save(room);

        SocketRoomDTO roomDTO = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), List.of());
        publishRoomSocket(roomDTO);
    }

    public void start(UUID roomId) throws Exception {
        Room room = getRoom(roomId);
        List<Music> musics = musicPlatformService.getRandomMusics(room.getSettings().getGenres());
        musics.forEach(music -> music.setId(UUIDService.generate(musicRepository)));
        musicRepository.saveAll(musics);
        room.setPlaying(true);
        room.setMusicsIds(musics.stream().map(Music::getId).collect(Collectors.toList()));
        roomRepository.save(room);

        publishRoomSocket(SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), musics));
    }

    public void getRoomInSocket(UUID roomId) throws NotFoundException {
        Room room = getRoom(roomId);
        publishRoomSocket(SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), getMusicsByIds(room.getMusicsIds())));
    }

    public GuessDTO guess(UUID roomId, GuessPayload payload) throws NotFoundException {
        GuessDTO response = new GuessDTO();
        Music musicToGuess = musicRepository.findById(payload.getMusicId()).orElseThrow(NotFoundException::new);
        int score = 0;

        String answer = normalize(payload.getAnswer());
        String title = normalize(musicToGuess.getTitleShort());
        String artist = normalize(musicToGuess.getArtist());

        if (answer.contains(title)) {
            score += 2;

            if (answer.contains(artist)) {
                score += 1;
            }

            Player player = playerRepository.findById(payload.getPlayerId()).orElseThrow(NotFoundException::new);
            player.setScore(player.getScore() + score);
            playerRepository.save(player);

            Room room = getRoom(roomId);
            SocketRoomDTO socketRoomDTO = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), getMusicsByIds(room.getMusicsIds()));
            publishRoomSocket(socketRoomDTO);

            response.setCorrectAnswer(true);
        } else {
            response.setCorrectAnswer(false);
        }

        return response;
    }

    private void publishRoomSocket(SocketRoomDTO room) {
        messagingTemplate.convertAndSend("/room/" + room.getId(), room);
    }

    private List<Player> getPlayersByIds(List<UUID> playersIds) {
        Iterable<Player> playersList = playerRepository.findAllById(playersIds);
        return StreamSupport.stream(playersList.spliterator(), false).collect(Collectors.toList());
    }

    private List<Music> getMusicsByIds(List<UUID> musicsIds) {
        Iterable<Music> musicsList = musicRepository.findAllById(musicsIds);
        return StreamSupport.stream(musicsList.spliterator(), false).collect(Collectors.toList());
    }

    private Room getRoom(UUID id) throws NotFoundException {
        return roomRepository.findById(id).orElseThrow(NotFoundException::new);
    }


    public static String normalize(String input) {
        if (input == null) return null;
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase(Locale.ROOT);
    }

}
