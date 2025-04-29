import type {Player} from "~/types/models";

const defaultPlayer: Player = {
    id: "",
    name: "",
    owner: false,
    score: 0
}

export const usePlayerStore = defineStore("player", () => {
    const player = ref(defaultPlayer);

    return {player}
}, {persist: true});