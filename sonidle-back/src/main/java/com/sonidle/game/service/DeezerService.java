package com.sonidle.game.service;

import com.sonidle.game.dto.DeezerGenre;
import com.sonidle.game.dto.DeezerChartPlaylists;
import com.sonidle.game.dto.DeezerPlaylistTracks;
import com.sonidle.game.dto.DeezerTrack;
import com.sonidle.game.model.Music;
import com.sonidle.game.model.MusicGenre;
import com.sonidle.game.service.interfaces.MusicPlatformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeezerService implements MusicPlatformService {
    private final WebClient webClient;
    private final static int NB_MUSICS_PER_GAME = 10;
    private final static int ALL_GENRES_ID = 0;

    public DeezerService(WebClient.Builder webClientBuilder, @Value("${deezer.api.url}") String deezerApiUrl) {
        this.webClient = webClientBuilder.baseUrl(deezerApiUrl)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                .build();
    }

    @Override
    public List<MusicGenre> getMusicGenres() {
        DeezerGenre response = webClient.get()
                .uri("/genre")
                .retrieve()
                .bodyToMono(DeezerGenre.class)
                .block();
        return response != null ? response.getData().stream().filter(genre -> genre.getId() != ALL_GENRES_ID).collect(Collectors.toList()) : List.of();
    }

    @Override
    public List<Music> getRandomMusics(List<MusicGenre> genres) throws Exception {
        List<Music> musics = new ArrayList<>();

        for (int i = 0; i < NB_MUSICS_PER_GAME; i++) {
            int randomGenreId = ALL_GENRES_ID;
            if (genres != null && !genres.isEmpty()) {
                int rand = (int) (Math.random() * genres.size());
                randomGenreId = genres.get(rand).getId();
            }
            long randomPlaylistId = getRandomPlaylistId(randomGenreId);
            Music randomMusic = getRandomMusicFromPlaylist(randomPlaylistId);
            musics.add(randomMusic);
        }

        return musics;
    }

    private long getRandomPlaylistId(int genreId) throws Exception {
        DeezerChartPlaylists response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/chart/{id}/playlists").queryParam("limit", 1000).build(genreId))
                .retrieve()
                .bodyToMono(DeezerChartPlaylists.class)
                .block();

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            throw new Exception("ERROR FROM DEEZER API");
        }

        int rand = (int) (Math.random() * response.getData().size());
        return response.getData().get(rand).getId();
    }

    private Music getRandomMusicFromPlaylist(long playlistId) {
        DeezerPlaylistTracks response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/playlist/{id}/tracks").queryParam("limit", 1000).build(playlistId))
                .retrieve()
                .bodyToMono(DeezerPlaylistTracks.class)
                .block();

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return new Music();
        }

        int rand = (int) (Math.random() * response.getData().size());
        DeezerTrack randomTrack = response.getData().get(rand);
        while (!randomTrack.isValid()) {
            rand = (int) (Math.random() * response.getData().size());
            randomTrack = response.getData().get(rand);
        }

        return randomTrack.toMusic();
    }
}
