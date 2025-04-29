<template>
  <UApp>
    <div v-if="route.path === '/'">
      <NuxtImg id="logo" src="logo2.png" quality="100" width="250"></NuxtImg>
      <div id="content" class="gap-8 mt-6">
        <div id="description" class="gap-2 text-center">
          <p class="text-2xl font-semibold">{{ $t('description') }}</p>
          <p class="text-sm font-light">{{ $t('sub_description') }}</p>
        </div>
        <UInput :placeholder="$t('nickname')" size="xl" v-model="playerName"></UInput>
        <div class="flex justify-center w-full gap-8">
          <UButton :label="$t('create_game')" :block="true" color="primary" size="xl" @click="openCreateGameModal"/>
          <UButton :label="$t('join_game')" :block="true" color="primary" size="xl" variant="outline"
                   @click="openJoinGameModal"/>
        </div>
      </div>
    </div>

    <NuxtPage v-else/>
  </UApp>
</template>

<script setup lang="ts">
import CreateGameModal from "~/components/home/createGameModal.vue";
import JoinGameModal from "~/components/home/joinGameModal.vue";
import type {CreateRoomPayload} from "~/types/payloads/createRoomPayload.js";
import roomService from "~/services/roomService";
import type {Room} from "~/types/models";
import {useRoomStore} from "~/stores/room";

const route = useRoute();
const {setLocale} = useI18n();
const overlay = useOverlay();
const router = useRouter();

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
    router.push(`/app/rooms`);
  })
}
</script>


<style>
#logo {
  @apply mx-auto;
}

#content {
  @apply mx-auto w-1/4 flex flex-col items-center justify-center;
}

#description {
  @apply flex flex-col items-center justify-center;
}
</style>