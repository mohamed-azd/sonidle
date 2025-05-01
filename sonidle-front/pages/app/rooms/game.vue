<script setup lang="ts">

import deezerService from "~/services/musicService";
import type {MusicGenre} from "~/types/models";
import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";
import { Client } from '@stomp/stompjs';
import {usePlaylistStore} from "~/stores/playlist";

let client : Client | undefined;

onMounted(() => {
  client = useRoomSocket(useRoomStore().room.id, (updatedRoom : any) => {
    useRoomStore().room = updatedRoom;

    if (updatedRoom.playlist != null && updatedRoom.playlist.length > 0) {
      usePlaylistStore().playlist = updatedRoom.playlist;
    }
  }, refreshRoom)
})

onUnmounted(() => {
  client?.deactivate();
});

function refreshRoom() {
  if (!client || !client.connected) return;

  const payload = {
    roomId: useRoomStore().room.id,
  }

  client.publish({
    destination: `/app/room`,
    body: JSON.stringify(payload)
  });
}
</script>

<template>
  <audio controls v-if="usePlaylistStore().playlist.length > 0">
    <source :src="usePlaylistStore().playlist[3].preview" />
  </audio>
</template>

<style scoped>

</style>