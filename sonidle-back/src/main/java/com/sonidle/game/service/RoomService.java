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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        publishRoomSocket(SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), List.of()));
    }

    public void nextRound(UUID roomId) throws NotFoundException {
        Room room = getRoom(roomId);

        if (!room.isGameStarted()) {
            room.setGameStarted(true);
        }

       List<Player> players = getPlayersByIds(room.getPlayersIds());
        players.forEach(player -> player.setGuessed(false));
        playerRepository.saveAll(players);

        List<UUID> musicIds = room.getMusicsIds();

        Music nextMusic = getMusicsByIds(musicIds).stream()
                .filter(music -> !music.isPlayed())
                .findFirst()
                .orElse(null);

        if (nextMusic == null) {
            SocketRoomDTO roomDto = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), getMusicsByIds(room.getMusicsIds()));
            messagingTemplate.convertAndSend("/room/" + roomId + "/end", roomDto);
            return;
        }

        nextMusic.setPlayed(true);
        musicRepository.save(nextMusic);
        room.setCurrentMusicId(nextMusic.getId());
        roomRepository.save(room);

        Map<String, Object> payload = new HashMap<>();
        long startTime = System.currentTimeMillis();
        int roundDuration = room.getSettings().getRoundDuration();
        payload.put("musicPreview", nextMusic.getPreview());
        payload.put("startTime", startTime);
        payload.put("duration", roundDuration);
        payload.put("currentMusicIndex", musicIds.indexOf(nextMusic.getId()) + 1);
        payload.put("nbMusics", musicIds.size());
        messagingTemplate.convertAndSend("/room/" + roomId + "/round/start", payload);

        SocketRoomDTO socketRoomDTO = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), List.of());
        publishRoomSocket(socketRoomDTO);

        long delayInMs = startTime + roundDuration * 1000L - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            messagingTemplate.convertAndSend("/room/" + roomId + "/round/end", nextMusic);
        }, delayInMs, TimeUnit.MILLISECONDS);
    }


    public GuessDTO guess(UUID roomId, GuessPayload payload) throws NotFoundException {
        GuessDTO response = new GuessDTO();
        Room room = getRoom(roomId);
        Player player = playerRepository.findById(payload.getPlayerId()).orElseThrow(NotFoundException::new);

        if (player.hasGuessed()) {
            response.setCorrectAnswer(false);
            return response;
        }

        Music musicToGuess = musicRepository.findById(room.getCurrentMusicId()).orElseThrow(NotFoundException::new);
        int score = 0;

        String answer = normalizeForGuess(payload.getAnswer());
        String expectedTitle = normalizeForGuess(musicToGuess.getTitleShort());
        String expectedArtist = normalizeForGuess(musicToGuess.getArtist());

        boolean titleMatched = strictMatch(answer, expectedTitle);
        boolean artistMatched = strictMatch(answer, expectedArtist);

        if (titleMatched) {
            score += 2;
            if (artistMatched) {
                score += 1;
            }

            player.setGuessed(true);
            player.setScore(player.getScore() + score);
            playerRepository.save(player);

            SocketRoomDTO socketRoomDTO = SocketRoomDTO.toDTO(room, getPlayersByIds(room.getPlayersIds()), List.of());
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


    private String normalizeForGuess(String input) {
        if (input == null) return "";

        input = input.replaceAll("\\(.*?\\)", "").replaceAll("\\[.*?\\]", "");

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        normalized = normalized.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]", "")
                .toLowerCase(Locale.ROOT);

        return normalized.trim().replaceAll("\\s+", " ");
    }



    private static boolean strictMatch(String guess, String expected) {
        Set<String> guessWords = new HashSet<>(Arrays.asList(guess.split(" ")));
        Set<String> expectedWords = new HashSet<>(Arrays.asList(expected.split(" ")));

        int common = 0;
        for (String word : expectedWords) {
            if (guessWords.contains(word)) {
                common++;
            }
        }

        double matchRatio = (double) common / expectedWords.size();
        return matchRatio >= 0.7;
    }
}
