package com.sonidle.game.controller;

import com.sonidle.game.payload.GetRoomPayload;
import com.sonidle.game.payload.UpdateGenresPayload;
import com.sonidle.game.service.RoomService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final RoomService roomService;

    public WebSocketController(RoomService roomService) {
        this.roomService = roomService;
    }

    @MessageMapping(value = "/room/genres")
    public void updateGenres(UpdateGenresPayload payload) throws NotFoundException {
        roomService.updateGenres(payload);
    }

    @MessageMapping(value = "/room")
    public void getRoom(GetRoomPayload payload) throws NotFoundException {
        roomService.getRoomInSocket(payload.getRoomId());
    }
}
