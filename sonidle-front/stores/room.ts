import type {Room} from "~/types/models";

const defaultRoom: Room = {
    id: "",
    players: [],
    settings: {
        id: "",
        gameDuration: 0,
        nbPlayerMax: 0,
        genres: []
    },
    playing: false
}

export const useRoomStore = defineStore("room", () => {
    const room = ref(defaultRoom);

    return {room}
}, {persist: true});