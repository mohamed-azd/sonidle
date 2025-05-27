<script setup lang="ts">
import { computed } from "vue";
import type {Music} from "~/types/models";
import roomService from "~/services/roomService";
import {useRoomStore} from "~/stores/room";

const props = defineProps<{
  modelValue: Boolean,
  music: Music
}>();
const emit = defineEmits(["update:modelValue"]);

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value)
});

function submit() {
  roomService.nextRound(useRoomStore().room.id);
  isOpen.value = false;
}
</script>

<template>
  <UModal v-model="isOpen" :close="false" :dismissible="false">
    <template #header>
      <h2>{{ $t("answer") }}</h2>
    </template>
    <template #body>
      <div class="flex flex-col items-center gap-6">
        <NuxtImg :src="music.picture " quality="100" class="w-1/2" />
        <div class="flex flex-col items-center">
          <p class="text-sm font-semibold">{{ music.title }}</p>
          <p class="text-sm font-light">{{ music.artist }}</p>
        </div>
      </div>
    </template>
    <template #footer v-if="usePlayerStore().player.owner">
      <div class="w-full flex justify-end">
        <UButton size="lg" :label="$t('next_round')" @click="submit" />
      </div>
    </template>
  </UModal>
</template>

<style scoped>
</style>
