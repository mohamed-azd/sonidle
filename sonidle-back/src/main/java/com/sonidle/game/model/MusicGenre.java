package com.sonidle.game.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

public enum MusicGenre implements Serializable {
    ROCK,
    RAP,
    JAZZ,
    CLASSICAL,
    POP,
    ELECTRONIC,
    REGGAE,
    BLUES,
    METAL,
    COUNTRY
}

