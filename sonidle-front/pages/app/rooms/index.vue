<script setup lang="ts">

import PlayerCard from "~/components/room/PlayerCard.vue";
import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";
import {Client} from '@stomp/stompjs';
import deezerService from "~/services/musicService";
import type {MusicGenre} from "~/types/models";
import roomService from "~/services/roomService";

type MusicGenreCheckBox = MusicGenre & {
  isSelected: boolean;
}

const router = useRouter();
const {t} = useI18n();

useHead({
  title: t('room_tab_title', {ownerName: useRoomStore().room.players.find(player => player.owner)?.name})
})

let client: Client | undefined;
const isRoomCodeCopied = ref(false)
const genres = ref<MusicGenreCheckBox[]>([]);
const isStartingGame = ref(false);
const gameStatus = computed(() => isStartingGame ? 'starting_game' : 'waiting_host');
const GAME_ROUTE = "/app/rooms/game"

function copyRoomCode() {
  navigator.clipboard.writeText(useRoomStore().room.id)
  isRoomCodeCopied.value = true

  setTimeout(() => {
    isRoomCodeCopied.value = false
  }, 2000)
}

onMounted(() => {
  if (useRoomStore().room.playing) {
    router.push(GAME_ROUTE);
  } else {
    deezerService.getGenres().then((data) => {
      const musicGenres = data.data.value as MusicGenre[];
      genres.value = musicGenres.map((genre) => {
        return {
          id: genre.id,
          name: genre.name,
          isSelected: false
        }
      });

      client = useRoomSocket({
        roomId: useRoomStore().room.id,
        onRoomUpdate : (updatedRoom) => {
          useRoomStore().room = updatedRoom;
          const selectedGenresIds = updatedRoom.settings.genres.map(selectedGenre => selectedGenre.id);
          genres.value.forEach((genre) => {
            genre.isSelected = selectedGenresIds.includes(genre.id);

          })
          if (useRoomStore().room.playing) {
            router.push(GAME_ROUTE);
          }
        }
      })
    })
  }
})

onUnmounted(() => {
  client?.deactivate();
});

watch(genres, (newValue) => {
  if (usePlayerStore().player.owner) {
    updateSelectedGenres();
  }
}, { deep: true });

function updateSelectedGenres() {
  if (!client || !client.connected) return;

  const payload = {
    roomId: useRoomStore().room.id,
    genres: genres.value.filter(genre => genre.isSelected),
  }

  client.publish({
    destination: `/app/room/genres`,
    body: JSON.stringify(payload)
  });
}

function start() {
  isStartingGame.value = true;
  roomService.start(useRoomStore().room.id).then(() => {
    router.push(GAME_ROUTE);
    isStartingGame.value = false;
  })
}
</script>

<template>
  <UButton
      :label="$t('copy_code')"
      :color="isRoomCodeCopied ? 'primary' : 'neutral'"
      variant="link"
      size="xl"
      :icon="isRoomCodeCopied ? 'i-lucide-copy-check' : 'i-lucide-copy'"
      @click="copyRoomCode"
  />
  <h1 class="text-2xl text-center font-medium">
    {{ $t('room_title', {ownerName: useRoomStore().room.players.find(player => player.owner)?.name}) }}
  </h1>

  <div class="w-full flex justify-between mt-10 h-[66vh]">
    <div class="flex flex-col w-1/2 items-center py-8 gap-6">
      <p class="text-lg font-semibold">{{ $t("choose_genres") }}</p>
      <div class="flex flex-wrap gap-4 w-full max-w-xl">
        <UCheckbox
            v-for="genre in genres"
            :key="genre.id"
            v-model="genre.isSelected"
            :label="genre.name"
            variant="card"
            size="xs"
            :disabled="!usePlayerStore().player.owner"
        />
      </div>
    </div>

    <USeparator orientation="vertical" class="h-[80%]"/>

    <div class="flex flex-col w-1/2 items-center py-8 gap-6">
      <p class="text-lg font-semibold">{{ $t('players') }}</p>
      <div id="players" class="flex flex-wrap gap-6 w-full max-w-xl justify-center">
        <div class="max-w-[5vw]" v-for="player in useRoomStore().room.players">
          <PlayerCard
              :key="player.id"
              :name="player.name"
              :owner="player.owner"
              :isCurrentPlayer="player.id === usePlayerStore().player.id" />
        </div>
      </div>
    </div>
  </div>

  <div class="w-full flex justify-center">
    <UButton v-if="usePlayerStore().player.owner" :label="$t('launch_game')" size="xl" trailing-icon="i-lucide:music-4" @click="start" :loading="isStartingGame"/>
    <p v-else class="text-center italic font-light text-lg">{{ $t(gameStatus) }}</p>
  </div>
</template>

<style scoped>
.player {
  @apply max-w-[7vw];
}
</style>