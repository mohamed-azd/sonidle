<script setup lang="ts">

import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";
import {Client} from '@stomp/stompjs';
import {usePlaylistStore} from "~/stores/playlist";
import GameTimer from "~/components/room/game/GameTimer.vue";
import roomService from "~/services/roomService";
import type {GuessResponseDTO} from "~/types/dtos/guessResponseDTO";
import PlayerCard from "~/components/room/PlayerCard.vue";
import RoundResultModal from "~/components/room/game/roundResultModal.vue";

const router = useRouter();
const overlay = useOverlay();

let client: Client | undefined;
let currentMusic: HTMLAudioElement | undefined;
const answer = ref("");
const isWrongAnswer = ref(false);
const resetTimerCounter = ref(0)
const startTime = ref(0);
const roundResultModal = overlay.create(RoundResultModal);


onMounted(() => {
  client = useRoomSocket({
    roomId: useRoomStore().room.id,
    onRoomUpdate: (updatedRoom: any) => {
      useRoomStore().room = updatedRoom;
      if (!useRoomStore().room.gameStarted && usePlayerStore().player.owner) {
        roomService.nextRound(useRoomStore().room.id)
      }
    },
    onRoundStart: (data) => {
      roundResultModal.close();
      startTimer(data.startTime, data.duration);
      playMusic(data.musicPreview);
    },
    onRoundEnd: (music) => {
      currentMusic?.remove();
      roundResultModal.open({
        music: music,
      })
    },
    onGameEnd: () => {
      router.push(`/rooms/end`);
    },
    onConnect: () => {
      refreshRoom();
    }
  })
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
  roomService.guess(useRoomStore().room.id, {
    answer: answer.value,
    playerId: usePlayerStore().player.id
  }).then(({data}) => {
    const response = data.value as GuessResponseDTO;
    if (response.correctAnswer) {
      isWrongAnswer.value = false;
    } else {
      isWrongAnswer.value = true;
    }
  })
}

function playMusic(url: string) {
  currentMusic = new Audio(url);
  currentMusic.play();
}

function startTimer(roundStartTime: number, duration: number) {
  startTime.value = roundStartTime;
  resetTimerCounter.value++
}
</script>

<template>
  <UApp>
    <div class="h-screen">
      <div class="absolute left-6 top-6 text-2xl text-primary font-bold flex items-center gap-2">
        <p class=" w-[2vw] h-[2vw] flex justify-center items-center text-xl">
          {{ usePlaylistStore().playlist.indexOf(usePlaylistStore().currentMusic) + 1 }}</p>
        <p class="text-primary-600"> / {{ usePlaylistStore().playlist.length }}</p>
      </div>
      <div class="h-1/6">
        <NuxtImg class="mx-auto h-full" src="logo2.png" quality="100"></NuxtImg>
      </div>

      <div class="flex flex-col items-center  h-5/6">
        <div class="flex flex-col gap-10 items-center justify-center w-full h-4/6">
          <GameTimer :duration="useRoomStore().room.settings.gameDuration ?? 30" :startTime="startTime"
                     :resetTrigger="resetTimerCounter"/>

          <div class="flex justify-center items-start gap-2 w-1/4">
            <UFormField class="flex-1" :error="isWrongAnswer">
              <UInput class="w-full" size="xl" :placeholder="$t('guess_placeholder')" v-model="answer"/>
            </UFormField>

            <UButton :label="$t('validate')" size="xl" @click="submitAnswer"/>
          </div>
        </div>

        <div class="h-3/6 flex justify-center scroll-auto gap-8 px-8 w-screen">
          <div :key="player.id" class="flex flex-col items-center h-full gap-6 justify-center max-w-[7vw] w-[7vw]"
               v-for="(player, index) in useRoomStore().room.players.sort((a, b) => b.score - a.score)">
            <PlayerCard
                :name="player.name"
                :owner="player.owner"
                :isCurrentPlayer="player.id === usePlayerStore().player.id"/>
            <p>
              <span class="text-primary-600 text-lg font-bold">{{ index + 1 }} - </span>
              <span class="text-lg">{{ $t('player_score', {score: player.score}) }}</span>
            </p>
          </div>
        </div>
      </div>

    </div>
  </UApp>
</template>

<style scoped>

</style>