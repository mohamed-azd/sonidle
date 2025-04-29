package com.sonidle.game.service;

import com.sonidle.game.dto.DeezerGenreResponseDTO;
import com.sonidle.game.model.Music;
import com.sonidle.game.model.MusicGenre;
import com.sonidle.game.service.interfaces.MusicPlatformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DeezerService implements MusicPlatformService {
    private final WebClient webClient;

    public DeezerService(WebClient.Builder webClientBuilder, @Value("${deezer.api.url}") String deezerApiUrl) {
        this.webClient = webClientBuilder.baseUrl(deezerApiUrl).build();
    }

    @Override
    public List<MusicGenre> getMusicGenres() {
        DeezerGenreResponseDTO response = webClient.get()
                .uri("/genre")
                .retrieve()
                .bodyToMono(DeezerGenreResponseDTO.class)
                .block();
        return response != null ? response.getData() : List.of();
    }

    @Override
    public List<Music> getRandomMusics(List<MusicGenre> genres) {
        return List.of();
    }
}
