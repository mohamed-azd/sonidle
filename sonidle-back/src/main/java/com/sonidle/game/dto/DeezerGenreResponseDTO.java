package com.sonidle.game.dto;

import com.sonidle.game.model.MusicGenre;

import java.util.List;

public class DeezerGenreResponseDTO {
    private List<MusicGenre> data;

    public List<MusicGenre> getData() {
        return data;
    }

    public void setData(List<MusicGenre> data) {
        this.data = data;
    }
}
