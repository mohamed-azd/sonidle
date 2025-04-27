package com.sonidle.game.payload;

import java.util.UUID;

public class JoinRoomPayload {
    private String playerName;
    private UUID roomId;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
}
