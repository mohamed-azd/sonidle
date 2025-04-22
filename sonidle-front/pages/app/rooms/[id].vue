<script setup lang="ts">

import PlayerCard from "~/components/room/PlayerCard.vue";
import {useRoomSocket} from "~/composables/useRoomSocket";

const route = useRoute();
const roomId: string = route.params.id;

onMounted( () => {
  console.log(route.params.id);
})

useRoomSocket(roomId, (room) => {
  console.log("WS ----------------")
  console.log(room)
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