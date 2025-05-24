package com.sonidle.game.controller;

import com.sonidle.game.dto.GuessDTO;
import com.sonidle.game.dto.SocketRoomDTO;
import com.sonidle.game.payload.CreateRoomPayload;
import com.sonidle.game.payload.GuessPayload;
import com.sonidle.game.payload.JoinRoomPayload;
import com.sonidle.game.service.RoomService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomRestController {
    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public SocketRoomDTO createRoom(@RequestBody CreateRoomPayload payload) {
        return roomService.create(payload);
    }

    @PostMapping("/join")
    public SocketRoomDTO joinRoom(@RequestBody JoinRoomPayload payload) throws NotFoundException, BadRequestException {
        return roomService.join(payload);
    }

    @PostMapping("/{id}/start")
    public void start(@PathVariable UUID id) throws Exception {
        roomService.start(id);
    }

    @PostMapping("/{id}/round/next")
    public void nextRound(@PathVariable UUID id) throws Exception {
        roomService.nextRound(id);
    }

    @PostMapping(value = "/{id}/guess")
    public GuessDTO guess(@PathVariable UUID id, @RequestBody GuessPayload payload) throws Exception {
        return roomService.guess(id, payload);
    }
}
