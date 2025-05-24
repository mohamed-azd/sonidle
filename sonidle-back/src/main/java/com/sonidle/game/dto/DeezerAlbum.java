package com.sonidle.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeezerAlbum {
    @JsonProperty("cover_medium")
    private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
