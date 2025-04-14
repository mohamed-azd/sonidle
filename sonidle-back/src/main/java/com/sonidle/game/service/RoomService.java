package com.sonidle.game.service;

import com.sonidle.game.model.Room;
import com.sonidle.game.repository.RoomRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoom(UUID id) throws NotFoundException {
        return roomRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
