package com.sonidle.game.controller;

import com.sonidle.game.dto.SocketRoomDTO;
import com.sonidle.game.model.Room;
import com.sonidle.game.payload.CreateRoomPayload;
import com.sonidle.game.payload.JoinRoomPayload;
import com.sonidle.game.service.RoomService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/{id}")
    public Room getRoom(@PathVariable UUID id) throws NotFoundException {
        return roomService.getRoom(id);
    }

    @PostMapping
    public SocketRoomDTO createRoom(@RequestBody CreateRoomPayload payload) {
        return roomService.create(payload);
    }

    @PostMapping("/join")
    public SocketRoomDTO joinRoom(@RequestBody JoinRoomPayload payload) throws NotFoundException, BadRequestException {
        return roomService.join(payload);
    }
}
