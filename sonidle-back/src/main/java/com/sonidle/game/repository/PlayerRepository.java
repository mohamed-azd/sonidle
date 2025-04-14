package com.sonidle.game.repository;

import com.sonidle.game.model.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlayerRepository extends CrudRepository<Player, UUID> {}
