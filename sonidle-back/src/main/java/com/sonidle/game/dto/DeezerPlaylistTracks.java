package com.sonidle.game.dto;

import com.sonidle.game.model.Music;
import com.sonidle.game.model.MusicGenre;

import java.util.List;

public class DeezerPlaylistTracks {
    private List<DeezerTrack> data;

    public List<DeezerTrack> getData() {
        return data;
    }

    public void setData(List<DeezerTrack> data) {
        this.data = data;
    }
}
