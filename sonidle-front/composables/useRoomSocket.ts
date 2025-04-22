import { Client } from '@stomp/stompjs';
import type {Room} from "~/types/models";

export const useRoomSocket = (roomId: string, onMessage: (room: Room) => void) => {
    const config = useRuntimeConfig()

    const client = new Client({
        brokerURL: config.public.wsUrl,
        reconnectDelay: 5000,
        onConnect: () => {
            client.subscribe(`/room/${roomId}`, (message) => {
                const roomData = JSON.parse(message.body);
                onMessage(roomData);
            });
        },
        onStompError: (frame) => {
            console.error('STOMP error:', frame);
        },
    });

    onMounted(() => client.activate());
    onUnmounted(() => client.deactivate());

    return { client };
};
