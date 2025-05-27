<script setup lang="ts">
import {useRoomStore} from "~/stores/room";
import {usePlayerStore} from "~/stores/player";

const {t} = useI18n();

useHead({
  title: t('results_tab_title')
})

onUnmounted(() => {
  useRoomStore().$reset()
  usePlayerStore().$reset()
})
</script>

<template>
  <div class="h-screen flex flex-col gap-6">
    <div class="h-1/6">
      <NuxtImg class="mx-auto h-full" src="logo2.png" quality="100"></NuxtImg>
    </div>

    <div class="w-1/5 mx-auto h-3/6 overflow-auto flex flex-col gap-2">
      <div v-for="(player, index) in useRoomStore().room.players.sort((a, b) => b.score - a.score)"
           class="grid grid-cols-[2rem_auto_6rem] items-center text-md" :key="player.id">
        <p class="text-primary font-bold">{{ index + 1 }} -</p>
        <p class="truncate">{{ player.name }}</p>
        <p class="text-primary font-light text-right">{{ player.score }} pts</p>
      </div>
    </div>

    <div class="h-2/6 w-3/4 mx-auto">
      <UCarousel :items="useRoomStore().room.playlist" v-slot="{ item, index }" :ui="{ item: 'basis-1/6' }" arrows wheel-gestures
                 :dragFree="true" :dragThreshold="10">
        <div class="flex flex-col w-full items-center gap-2 text-center relative cursor-pointer">
          <img :src="item.picture" width="200" height="200" class="rounded-lg">
          <p class="absolute top-0 left-2 font-bold bg-primary rounded-full w-[1.5vw] h-[1.5vw] flex justify-center items-center">
            {{ index + 1 }}
          </p>
          <div class="flex flex-col items-center">
            <p class="text-sm">{{ item.titleShort }}</p>
            <p class="text-xs font-light">{{ item.artist }}</p>
          </div>
        </div>
      </UCarousel>
    </div>
  </div>
</template>

<style scoped>

</style>