<script setup lang="ts">

import PlayerCard from "~/components/room/PlayerCard.vue";
import {useRoomSocket} from "~/composables/useRoomSocket";
import type {Room} from "~/types/models";
import {useRoomStore} from "~/stores/room";

const { $pinia } = useNuxtApp();

onMounted(() => {
  useRoomSocket(useRoomStore().room.id, (updatedRoom) => {
    console.log(updatedRoom);
  })

  console.log(useRoomStore().room.id);
})
</script>

<template>
  <div id="players" class="mt-5">
    <div class="player">
      <PlayerCard name="Pseudo 1"></PlayerCard>
    </div>
    <div class="player">
      <PlayerCard name="Pseudo 1"></PlayerCard>
    </div>
    <div class="player">
      <PlayerCard name="Pseudo 1"></PlayerCard>
    </div>
    <div class="player">
      <PlayerCard name="Pseudo 1"></PlayerCard>
    </div>
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