package com.sonidle.game.payload;

import java.util.UUID;

public class GetRoomPayload {
    private UUID roomId;

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
}
