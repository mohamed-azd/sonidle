<script setup lang="ts">

defineProps({
  name: {
    type: String,
    required: true
  },
  owner: {
    type: Boolean,
    required: false
  },
  isCurrentPlayer: {
    type: Boolean,
    required: true
  }
})

</script>

<template>
  <UCard>
    <NuxtImg id="playerAvatar" src="ez.png" class="rounded-t-lg"/>
    <template #footer>
      <div class="flex justify-center items-center gap-2 px-2">
        <UIcon name="mdi:crown" class="text-yellow-400 text-md" v-if="owner"/>
        <p :class="isCurrentPlayer ? 'text-sm truncated-text text-primary font-semibold' : 'text-sm truncated-text'">{{ name }}</p>
      </div>
    </template>
  </UCard>
</template>

<style scoped>
#playerAvatar {
  @apply w-full;
}

.truncated-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
</style>