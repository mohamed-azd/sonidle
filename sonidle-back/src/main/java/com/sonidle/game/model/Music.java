package com.sonidle.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("Music")
public class Music implements Serializable {
    @Id
    private UUID id;
    private String url;
    private String title;
    private String artist;
    private String album;
    private MusicGenre genre;
}
