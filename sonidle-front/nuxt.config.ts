// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  modules: ['@nuxt/icon', '@nuxt/ui', '@nuxt/fonts'],
  css: ['assets/css/main.css'],
  ssr: true,
  routeRules: {
    '/': { prerender: true },
    '/app/**': { ssr: false },
  },
})