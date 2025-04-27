<script setup lang="ts">

import PlayerCard from "~/components/room/PlayerCard.vue";
import {useRoomSocket} from "~/composables/useRoomSocket";
import {useRoomStore} from "~/stores/room";

const isRoomCodeCopied = ref(false)

function copyRoomCode() {
  navigator.clipboard.writeText(useRoomStore().room.id)
  isRoomCodeCopied.value = true

  setTimeout(() => {
    isRoomCodeCopied.value = false
  }, 2000)
}

onMounted(() => {
  useRoomSocket(useRoomStore().room.id, (updatedRoom) => {
    console.log(updatedRoom);
  })
})
</script>

<template>
  <UButton
      :label="$t('copy_code')"
      :color="isRoomCodeCopied ? 'success' : 'neutral'"
      variant="link"
      size="lg"
      :icon="isRoomCodeCopied ? 'i-lucide-copy-check' : 'i-lucide-copy'"
      @click="copyRoomCode"
  />
  <div id="players" class="mt-5">
    <div class="player" v-for="player in useRoomStore().room.players">
      <PlayerCard :name="player.name" :owner="player.owner"></PlayerCard>
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