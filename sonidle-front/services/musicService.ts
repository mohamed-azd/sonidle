import {useApi} from "~/services/useApi";
import type {MusicGenre} from "~/types/models";

export default {
    async getGenres() {
        return await useApi<MusicGenre[]>(`/musics/genres`);
    },
}