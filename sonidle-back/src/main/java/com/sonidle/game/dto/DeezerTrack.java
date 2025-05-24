package com.sonidle.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sonidle.game.model.Music;

public class DeezerTrack {
    private long id;
    private String title;
    @JsonProperty("title_short")
    private String titleShort;
    private String preview;
    private DeezerArtist artist;
    private DeezerAlbum album;

    public Music toMusic() {
        Music music = new Music();
        music.setTitle(title);
        music.setTitleShort(titleShort);
        music.setArtist(artist.getName());
        music.setPreview(preview);

        if (album != null) {
            music.setPicture(album.getCover());
        } else {
            music.setPicture(artist.getPicture());
        }

        return music;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public DeezerArtist getArtist() {
        return artist;
    }

    public void setArtist(DeezerArtist artist) {
        this.artist = artist;
    }

    public DeezerAlbum getAlbum() {
        return album;
    }

    public void setAlbum(DeezerAlbum album) {
        this.album = album;
    }

    public boolean isValid() {
        return title != null && !title.isEmpty()
                && preview != null && !preview.isEmpty()
                && artist != null && artist.getName() != null && !artist.getName().isEmpty();
    }
}
