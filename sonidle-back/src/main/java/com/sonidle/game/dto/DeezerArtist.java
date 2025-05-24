package com.sonidle.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeezerArtist {
    private String name;
    @JsonProperty("picture_medium")
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
