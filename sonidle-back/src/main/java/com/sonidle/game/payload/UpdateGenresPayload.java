package com.sonidle.game.payload;

import com.sonidle.game.model.MusicGenre;

import java.util.List;
import java.util.UUID;

public class UpdateGenresPayload {
    UUID roomId;
    List<MusicGenre> genres;

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public List<MusicGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicGenre> genres) {
        this.genres = genres;
    }
}
