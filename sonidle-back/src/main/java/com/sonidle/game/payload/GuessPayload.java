package com.sonidle.game.payload;

import java.util.UUID;

public class GuessPayload {
    private UUID playerId;
    private String answer;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer.toLowerCase();
    }
}
