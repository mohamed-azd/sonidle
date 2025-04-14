package com.sonidle.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RedisHash("RoomSettings")
public class RoomSettings implements Serializable {
    @Id
    private UUID id;
    private int nbPlayersMax;
    private int gameDuration;
    private List<MusicGenre> genres;
}
