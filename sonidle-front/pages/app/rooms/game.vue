<script setup lang="ts">

import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";
import {Client} from '@stomp/stompjs';
import {usePlaylistStore} from "~/stores/playlist";
import GameTimer from "~/components/room/game/GameTimer.vue";
import roomService from "~/services/roomService";
import type {GuessResponseDTO} from "~/types/dtos/guessResponseDTO";

let client: Client | undefined;
const answer = ref("");
const isWrongAnswer = ref(false);

onMounted(() => {
  client = useRoomSocket(useRoomStore().room.id, (updatedRoom: any) => {
    useRoomStore().room = updatedRoom;

    if (updatedRoom.playlist != null && updatedRoom.playlist.length > 0) {
      usePlaylistStore().playlist = updatedRoom.playlist;
      usePlaylistStore().currentMusic = updatedRoom.playlist[0];
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

function submitAnswer() {
  isWrongAnswer.value = true;
  roomService.guess(useRoomStore().room.id, {
    answer: answer.value,
    musicId: usePlaylistStore().currentMusic?.id
  }).then((response: GuessResponseDTO) => {
    console.log(response)
  })
}
</script>

<template>
  <div class="h-screen">
    <p class="absolute left-6 top-6 text-2xl text-primary font-bold">1 <span class="text-primary-600"> / 10</span></p>
    <div class="h-1/6">
      <NuxtImg class="mx-auto h-full" src="logo2.png" quality="100"></NuxtImg>
    </div>

    <div class="flex flex-col items-center gap-8 h-5/6">
      <div class="flex flex-col gap-8 items-center justify-center w-full h-4/6">
        <GameTimer :duration="30"/>
        <audio class="text-primary" controls v-if="usePlaylistStore().playlist.length > 0">
          <source :src="usePlaylistStore().currentMusic?.preview"/>
        </audio>
      </div>

      <div class="h-1/6 flex justify-center items-start gap-2 w-1/4">
        <UFormField class="flex-1" :error="isWrongAnswer">
          <UInput class="w-full" size="xl" :placeholder="$t('guess_placeholder')" v-model="answer"/>
        </UFormField>

        <UButton :label="$t('validate')" size="xl" @click="submitAnswer"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>