package com.sonidle.game.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("Player")
public class Player implements Serializable {
    @Id
    private UUID id;
    private String name;
    private int score;
}
