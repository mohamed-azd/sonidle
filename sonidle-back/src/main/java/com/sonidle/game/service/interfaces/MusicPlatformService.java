package com.sonidle.game.service.interfaces;

import com.sonidle.game.model.Music;
import com.sonidle.game.model.MusicGenre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MusicPlatformService {
    List<MusicGenre> getMusicGenres();

    List<Music> getRandomMusics(List<MusicGenre> genres) throws Exception;
}
