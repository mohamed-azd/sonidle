import type {Music} from "~/types/models";

export const usePlaylistStore = defineStore("playlist", () => {
    const playlist = ref<Music[]>([]);
    return {playlist}
});