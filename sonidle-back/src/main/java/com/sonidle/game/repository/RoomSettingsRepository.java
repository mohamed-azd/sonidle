package com.sonidle.game.repository;

import com.sonidle.game.model.RoomSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoomSettingsRepository extends CrudRepository<RoomSettings, UUID> {}
