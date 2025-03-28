<template>
  <div v-if="route.path === '/'">
    <h1>Page d'accueil (SSR/SSG)</h1>
    <NuxtLink to="/app">Accéder à l'app SPA</NuxtLink>
  </div>

  <NuxtPage v-else />
</template>

<script setup>
const route = useRoute()
</script>