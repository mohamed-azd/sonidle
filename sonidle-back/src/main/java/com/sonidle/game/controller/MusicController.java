package com.sonidle.game.controller;

import com.sonidle.game.model.MusicGenre;
import com.sonidle.game.service.interfaces.MusicPlatformService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/musics")
public class MusicController {
    private final MusicPlatformService musicPlatformService;

    public MusicController(MusicPlatformService musicPlatformService) {
        this.musicPlatformService = musicPlatformService;
    }

    @GetMapping("/genres")
    public List<MusicGenre> getGenres() {
        return musicPlatformService.getMusicGenres();
    }
}
