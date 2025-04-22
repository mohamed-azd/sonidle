<script setup lang="ts">
import { computed } from "vue";

const props = defineProps({
  modelValue: Boolean,
  onCreateGame: Function,
});
const emit = defineEmits(["update:modelValue"]);

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value)
});

const nbPlayers = ref(2)
const roundDuration = ref(30)

function submit() {
  props.onCreateGame(nbPlayers.value, roundDuration.value);
  isOpen.value = false;
}
</script>

<template>
  <UModal v-model="isOpen" :close="true">
    <template #header>
      <h2>{{ $t("create_game") }}</h2>
    </template>
    <template #body>
      <div class="w-full flex flex-col gap-6 px-6">
        <div class="createGameModalParameter px-4">
          <label class="w-1/2">{{$t('players_number')}}</label>
          <UInputNumber class="w-1/2" v-model="nbPlayers" :min=2 :max=20 />
        </div>
        <div class="createGameModalParameter px-4">
          <label class="w-1/2">{{`${$t('round_duration')} `}} <span class="font-semibold">{{`(${roundDuration}s)`}}</span></label>
          <USlider class="w-1/2" v-model="roundDuration" :min=5 :max=30 />
        </div>
      </div>
    </template>
    <template #footer>
      <div class="w-full flex justify-end">
        <UButton size="lg" :label="$t('confirm')" @click="submit" />
      </div>
    </template>
  </UModal>
</template>

<style scoped>
.createGameModalParameter {
  @apply w-full flex justify-between;
}
</style>
