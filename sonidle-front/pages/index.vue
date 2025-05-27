<script setup lang="ts">
import CreateGameModal from "~/components/home/createGameModal.vue";
import JoinGameModal from "~/components/home/joinGameModal.vue";
import type {CreateRoomPayload} from "~/types/payloads/createRoomPayload.js";
import roomService from "~/services/roomService";
import type {Player, Room} from "~/types/models";
import {useRoomStore} from "~/stores/room";
import {usePlayerStore} from "~/stores/player";

const {setLocale, t} = useI18n();
const route = useRoute();
const overlay = useOverlay();
const router = useRouter();

useHead({
  title: t('home_tab_title')
})

const createGameModal = overlay.create(CreateGameModal);
const joinGameModal = overlay.create(JoinGameModal);

const playerName = ref("");

function openCreateGameModal() {
  createGameModal.open({
    onCreateGame: createGame
  });
}

function openJoinGameModal() {
  joinGameModal.open({
    onJoinGame: joinGame
  });
}

async function createGame(nbPlayersMax: number, roundDuration: number) {
  roomService.create({
    playerName: playerName.value,
    nbPlayersMax,
    roundDuration
  }).then((data) => {
    const room = data.data.value as Room;
    useRoomStore().room = room;
    usePlayerStore().player = room.players.find(player => player.name === playerName.value) as Player;
    router.push(`/app/rooms`);
  });
}

function joinGame(roomId: string) {
  roomService.join({
    playerName: playerName.value,
    roomId: roomId
  }).then((data) => {
    const room = data.data.value as Room;
    useRoomStore().room = room;
    usePlayerStore().player = room.players.find(player => player.name === playerName.value);
    router.push(`/app/rooms`);
  })
}
</script>

<template>
  <UApp>
    <div v-if="route.path === '/'">
      <NuxtImg id="logo" src="logo2.png" width="250" format="png" loading="lazy" sizes="(max-width: 768px) 150px, 250px"/>
      <div class="gap-8 mt-6 mx-auto w-full max-w-md flex flex-col items-center justify-center">
        <div id="description" class="gap-2 text-center w-3/4 sm:w-full">
          <p class="sm:text-2xl font-semibold">{{ $t('description') }}</p>
          <p class="sm:text-sm text-xs font-light">{{ $t('sub_description') }}</p>
        </div>
        <UInput :placeholder="$t('nickname')" class="w-1/2 sm-w-full text-xs sm:text-xl" size="lg" v-model="playerName"></UInput>
        <div class="flex flex-col sm:flex-row justify-center w-5/12 sm:w-full gap-8">
          <UButton :label="$t('create_game')" :block="true" size="lg" color="primary" @click="openCreateGameModal"/>
          <UButton :label="$t('join_game')" :block="true" size="lg" color="primary" variant="outline"
                   @click="openJoinGameModal"/>
        </div>
      </div>
    </div>

    <NuxtPage v-else/>
  </UApp>
</template>


<style>
#logo {
  @apply mx-auto;
}

#description {
  @apply flex flex-col items-center justify-center;
}
</style>