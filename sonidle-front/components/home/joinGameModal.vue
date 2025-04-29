<script setup lang="ts">
import {computed} from "vue";

const props = defineProps({
  modelValue: Boolean,
  onJoinGame: Function
});
const emit = defineEmits(["update:modelValue"]);

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value)
});

const roomId = ref("")

function submit() {
  props.onJoinGame(roomId.value);
  isOpen.value = false;
}
</script>

<template>
  <UModal v-model="isOpen" :close="true">
    <template #header>
      <h2>{{ $t("join_game") }}</h2>
    </template>
    <template #body>
      <div class="w-full flex items-center justify-between px-6">
        <label class="w-1/3">{{ $t('code') }}</label>
        <UInput v-model="roomId" class="w-2/3" :placeholder="$t('paste_game_code')" size="lg"/>
      </div>
    </template>
    <template #footer>
      <div class="w-full flex justify-end">
        <UButton @click="submit" size="lg" :label="$t('join')"></UButton>
      </div>
    </template>
  </UModal>
</template>

<style scoped>
</style>