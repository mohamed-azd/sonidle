package com.sonidle.game.payload;

import java.util.UUID;

public class GuessPayload {
    private UUID playerId;
    private UUID musicId;
    private String answer;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getMusicId() {
        return musicId;
    }

    public void setMusicId(UUID musicId) {
        this.musicId = musicId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer.toLowerCase();
    }
}
