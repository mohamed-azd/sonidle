package com.sonidle.game.repository;

import com.sonidle.game.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoomRepository extends CrudRepository<Room, UUID> {}
