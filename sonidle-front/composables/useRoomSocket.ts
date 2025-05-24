import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import type { Room, Music } from '~/types/models'
import type {RoundStartDTO} from "~/types/dtos/roundStartDTO";

type UseRoomSocketOptions = {
    roomId: string
    onRoomUpdate: (room: Room) => void
    onRoundStart?: (data: RoundStartDTO) => void
    onRoundEnd?: (music: Music) => void
    onGameEnd?: () => void
    onConnect?: () => void
}

export const useRoomSocket = ({
                                  roomId,
                                  onRoomUpdate,
                                  onRoundStart,
                                  onRoundEnd,
                                  onGameEnd,
                                  onConnect,
                              }: UseRoomSocketOptions): Client | undefined => {
    const config = useRuntimeConfig()
    const socketUrl = config.public.wsUrl

    if (!import.meta.client) {
        return
    }

    const socket = new SockJS(socketUrl)

    const client = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        onConnect: () => {
            client.subscribe(`/room/${roomId}`, (message) => {
                const roomData: Room = JSON.parse(message.body)
                onRoomUpdate(roomData)
            })

            if (onRoundStart) {
                client.subscribe(`/room/${roomId}/round/start`, (message) => {
                    const data: RoundStartDTO = JSON.parse(message.body)
                    onRoundStart(data)
                })
            }

            if (onRoundEnd) {
                client.subscribe(`/room/${roomId}/round/end`, (message) => {
                    const music: Music = JSON.parse(message.body)
                    onRoundEnd(music)
                })
            }

            if (onGameEnd) {
                client.subscribe(`/room/${roomId}/end`, () => {
                    onGameEnd()
                })
            }

            if (onConnect) onConnect()
        },
        onStompError: (frame) => {
            console.error('STOMP error:', frame)
        },
    })

    client.activate()

    return client
}