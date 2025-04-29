<script setup lang="ts">

import PlayerCard from "~/components/room/PlayerCard.vue";
import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";
import {Client} from '@stomp/stompjs';
import deezerService from "~/services/musicService";
import type {MusicGenre} from "~/types/models";

type MusicGenreCheckBox = MusicGenre & {
  isSelected: boolean;
}

let client: Client | undefined;
const isRoomCodeCopied = ref(false)
const genres = ref<MusicGenreCheckBox[]>([]);

function copyRoomCode() {
  navigator.clipboard.writeText(useRoomStore().room.id)
  isRoomCodeCopied.value = true

  setTimeout(() => {
    isRoomCodeCopied.value = false
  }, 2000)
}

onMounted(() => {
  deezerService.getGenres().then((data) => {
    const musicGenres: MusicGenre[] = data.data.value;
    genres.value = musicGenres.map((genre) => {
      return {
        id: genre.id,
        name: genre.name,
        isSelected: false
      }
    });

    client = useRoomSocket(useRoomStore().room.id, (updatedRoom) => {
      useRoomStore().room = updatedRoom;
      const currentMusicGenres = genres.value;
      for (const selectedGenre of updatedRoom.settings.genres) {
        currentMusicGenres.filter(genre )
      }

      console.log(selectedGenreIds.value)
    })
  })
})

onUnmounted(() => {
  client?.deactivate();
});

watch(selectedGenreIds, (newValue) => {
  console.log('salut')
  if (usePlayerStore().player.owner) {
    updateSelectedGenres(newValue);
  }
});

function updateSelectedGenres() {
  console.log('caca')
  if (!client) return;

  const payload = {
    roomId: useRoomStore().room.id,
    genres: genres.value.filter(genre => genre.isSelected),
  }

  client.publish({
    destination: `/app/room/${useRoomStore().room.id}/genres`,
    body: JSON.stringify(payload)
  });
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
  <h1 class="text-xl text-center font-medium">
    {{ $t('room_title', {ownerName: useRoomStore().room.players.find(player => player.owner).name}) }}
  </h1>

  <div class="w-full flex justify-between mt-8 h-[66vh]">
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

    <USeparator orientation="vertical"/>

    <div class="flex flex-col w-1/2 items-center py-8 gap-6">
      <p class="text-lg font-semibold">{{ $t('players') }}</p>
      <div id="players">
        <div class="player" v-for="player in useRoomStore().room.players">
          <PlayerCard :key="player.id" :name="player.name" :owner="player.owner"></PlayerCard>
        </div>
      </div>
    </div>
  </div>

  <div class="w-full flex justify-center mt-8">
    <UButton :label="$t('launch_game')" size="xl"/>
  </div>
</template>

<style scoped>

#players {
  @apply w-[40vw] mx-auto grid grid-cols-4 place-items-center;
}

.player {
  @apply max-w-[7vw];
}
</style>