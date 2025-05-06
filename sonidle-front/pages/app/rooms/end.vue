<script setup lang="ts">

onUnmounted(() => {
  useRoomStore().$reset()
  usePlayerStore().$reset()
  usePlaylistStore().$reset()
})
</script>

<template>
</template>

<style scoped>

</style>