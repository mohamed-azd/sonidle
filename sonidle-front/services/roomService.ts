import {useApi} from "~/services/useApi";
import type {Room} from "~/types/models";
import type {CreateRoomPayload} from "~/types/payloads/createRoomPayload";

export default {
    async get(id: number) {
        return await useApi<Room>(`/rooms/${id}`);
    },

    async create(payload : CreateRoomPayload) {
        return await useApi<Room>("/rooms", { method: "POST", body: payload });
    },

    async join(payload: JoinGamePaylaod) {
        return await useApi<Room>("/rooms/join", { method: "POST", body: payload });
    }
}