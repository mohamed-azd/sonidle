package com.sonidle.game.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

public class UUIDService {

    public static <T> UUID generate(CrudRepository<T, UUID> repository) {
        UUID uuid = UUID.randomUUID();
        while (repository.existsById(uuid)) {
            uuid = UUID.randomUUID();
        }

        return uuid;
    }
}
