import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client'
import type {Room} from "~/types/models";

export const useRoomSocket = (roomId: string, onMessage: (room: Room) => void): Client | undefined => {
    const config = useRuntimeConfig()
    const socketUrl = config.public.wsUrl;

    if (!import.meta.client) {
        return;
    }
    const socket = new SockJS(socketUrl);

    const client = new Client({
        webSocketFactory: () => socket,
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

    return client;
};
