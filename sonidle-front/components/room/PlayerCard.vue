<script setup lang="ts">

defineProps({
  name: {
    type: String,
    required: true
  },
  owner: {
    type: Boolean,
    required: false
  }
})

</script>

<template>
  <UCard>
    <NuxtImg id="playerAvatar" src="avatar.svg" class="rounded-t-lg"/>
    <template #footer>
      <div class="text-center text-wrap flex justify-center gap-2">
        <UIcon name="mdi:crown" class="text-yellow-400" v-if="owner"/>
        <p class="text-sm truncated-text">{{ name }}</p>
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