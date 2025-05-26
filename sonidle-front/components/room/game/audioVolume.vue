<script setup lang="ts">
import {useUserPreferencesStore} from "~/stores/userPreferences";

const props = defineProps<{
  audio: HTMLAudioElement
}>()

const userPreferences = useUserPreferencesStore();
const volume = ref(userPreferences.volume);

watch(volume, (newVal, oldVal) => {
  userPreferences.volume = newVal;

  if (props.audio) {
    props.audio.volume = newVal;
  }
});
</script>

<template>
<div class="flex w-full gap-4">
  <UIcon :name="volume ? 'i-lucide-volume-2' : 'i-lucide-volume-x'" size="25" />
  <USlider v-model="volume" :min="0" :max="1" :step="0.1"/>
</div>
</template>

<style scoped>

</style>