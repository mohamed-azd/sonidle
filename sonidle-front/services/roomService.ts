import {useApi} from "~/services/useApi";
import type {Room} from "~/types/models";
import type {CreateRoomPayload} from "~/types/payloads/createRoomPayload";
import type {JoinRoomPayload} from "~/types/payloads/joinGamePayload";
import type {GuessPayload} from "~/types/payloads/guessPayload";
import type {GuessResponseDTO} from "~/types/dtos/guessResponseDTO";

export default {
    async get(id: number) {
        return await useApi<Room>(`/rooms/${id}`);
    },

    async create(payload : CreateRoomPayload) {
        return await useApi<Room>("/rooms", { method: "POST", body: payload });
    },

    async join(payload: JoinRoomPayload) {
        return await useApi<Room>("/rooms/join", { method: "POST", body: payload });
    },

    async start(roomId: string) {
        return await useApi<Room>(`/rooms/${roomId}/start`, { method: "POST" });
    },

    async guess(roomId: string, payload: GuessPayload) {
        return await useApi<GuessResponseDTO>(`/rooms/${roomId}/guess`, { method: "POST", body: payload });
    },

    async nextRound(roomId: string) {
        return await useApi<void>(`/rooms/${roomId}/round/next`, { method: "POST" });
    }
}