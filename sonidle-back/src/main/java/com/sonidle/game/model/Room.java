package com.sonidle.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RedisHash("Room")
public class Room implements Serializable {
    @Id
    private UUID id;
    private String name;
    private RoomSettings settings;
    private List<UUID> playersIds;
    private List<UUID> musicsIds;
    private boolean isPlaying;

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

    public List<UUID> getPlayersIds() {
        return playersIds;
    }

    public void setPlayersIds(List<UUID> playersIds) {
        this.playersIds = playersIds;
    }

    public List<UUID> getMusicsIds() {
        return musicsIds;
    }

    public void setMusicsIds(List<UUID> musicsIds) {
        this.musicsIds = musicsIds;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
