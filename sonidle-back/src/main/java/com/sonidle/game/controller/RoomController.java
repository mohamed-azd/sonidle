package com.sonidle.game.controller;

import com.sonidle.game.model.Room;
import com.sonidle.game.repository.RoomRepository;
import com.sonidle.game.service.RoomService;
import org.springframework.data.crossstore.ChangeSetPersister.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
