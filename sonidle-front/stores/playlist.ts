import type {Music} from "~/types/models";

export const usePlaylistStore = defineStore("playlist", () => {
    const playlist = ref<Music[]>([]);
    const currentMusic = ref<Music | undefined>();

    return { playlist, currentMusic }
});