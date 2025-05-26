<script setup lang="ts">
const props = defineProps({
  startTime: Number,
  duration: Number,
  resetTrigger: Number,
})

const emit = defineEmits<{
  (e: 'finished'): void;
}>();

const timer = ref(0)
let interval : NodeJS.Timeout;

const radius = 45
const circumference = 2 * Math.PI * radius

const strokeOffset = computed(() => {
  const percent = timer.value / props.duration
  return circumference * (1 - percent)
})


watch(() => props.resetTrigger, (newValue) => {
  if (newValue && newValue > 0) {
    startTimer();
  }
}, { immediate: true });

function startTimer() {
  clearInterval(interval)
  const wait = props.startTime - Date.now()

  setTimeout(() => {
    interval = setInterval(() => {
      const now = Date.now()
      const endTime = props.startTime + props.duration * 1000
      const secondsLeft = Math.ceil((endTime - now) / 1000)
      timer.value = Math.max(0, secondsLeft)

      if (secondsLeft <= 0) {
        clearInterval(interval)
        emit('finished')
      }
    }, 1000)
  }, Math.max(0, wait))
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

    <p class="absolute text-4xl font-bold text-primary" v-if="timer">
      {{ timer }}
    </p>
    <UIcon name="i-lucide-loader-circle" class="animate-spin absolute text-4xl font-bold text-primary" v-else/>
  </div>
</template>



<style scoped>

</style>