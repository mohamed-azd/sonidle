<script setup lang="ts">
const props = defineProps({
  duration: Number,
  resetTrigger: Number,
})

const emit = defineEmits<{
  (e: 'finished'): void;
}>();

const timer = ref(props.duration)
let interval;

const radius = 45
const circumference = 2 * Math.PI * radius

const strokeOffset = computed(() => {
  const percent = timer.value / props.duration
  return circumference * (1 - percent)
})


watch(() => props.resetTrigger, (newValue) => {
  if (newValue > 0) {
    startTimer();
  }
}, { immediate: true });

function startTimer() {
  clearInterval(interval)
  timer.value = props.duration
  interval = setInterval(() => {
    if (timer.value > 0) {
      timer.value -= 1
    } else {
      clearInterval(interval)
      emit('finished')
    }
  }, 1000)
}

onUnmounted(() => {
  clearInterval(interval)
})
</script>

<template>
  <div class="w-[10vw] h-[10vw] relative flex items-center justify-center">
    <svg
        class="w-full h-full transform -rotate-90 "
        viewBox="0 0 100 100"
    >

      <circle
          cx="50"
          cy="50"
          r="45"
          class="stroke-primary-700"
          stroke-width="5"
          fill="none"
      />

      <circle
          cx="50"
          cy="50"
          r="45"
          class="stroke-primary"
          stroke-width="5"
          fill="none"
          stroke-linecap="round"
          :stroke-dasharray="circumference"
          :stroke-dashoffset="strokeOffset"
          style="transition: stroke-dashoffset 1s linear;"
      />
    </svg>

    <p class="absolute text-4xl font-bold text-primary">
      {{ timer }}
    </p>
  </div>
</template>



<style scoped>

</style>