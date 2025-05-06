package com.sonidle.game.dto;

import com.sonidle.game.model.*;

import java.util.List;
import java.util.UUID;

public class SocketRoomDTO {
    private UUID id;
    private String name;
    private RoomSettings settings;
    private List<Player> players;
    private List<Music> playlist;
    private boolean isPlaying;

    public static SocketRoomDTO toDTO(Room room, List<Player> players, List<Music> musics) {
        SocketRoomDTO dto = new SocketRoomDTO();
        dto.id = room.getId();
        dto.name = room.getName();
        dto.settings = room.getSettings();
        dto.players = players;
        dto.playlist = musics;
        dto.isPlaying = room.isPlaying();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomSettings getSettings() {
        return settings;
    }

    public void setSettings(RoomSettings settings) {
        this.settings = settings;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Music> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Music> playlist) {
        this.playlist = playlist;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
