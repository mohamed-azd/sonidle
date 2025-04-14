package com.sonidle.game.repository;

import com.sonidle.game.model.Music;
import com.sonidle.game.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MusicRepository extends CrudRepository<Music, UUID> {}
