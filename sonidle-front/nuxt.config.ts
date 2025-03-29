// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    compatibilityDate: '2024-11-01',
    devtools: {enabled: true},
    modules: ['@nuxt/icon', '@nuxt/ui', '@nuxt/fonts', '@nuxt/image', '@nuxtjs/i18n'],
    css: ['assets/css/main.css'],
    ssr: true,
    routeRules: {
        '/': {prerender: true},
        '/app/**': {ssr: false},
    },
    ui: {
        theme: {
            colors: ['primary', 'secondary', 'success', 'info', 'warning', 'error', 'customPrimary', 'customSecondary', 'customNeutral'],
        }
    },
    i18n: {
        langDir: 'locales',
        lazy: true,
        strategy: 'no_prefix',
        locales: [
            {
                code: "en-US",
                iso: "en-US",
                name: "English(US)",
                file: "en-US.json",
            },
            {
                code: "fr-FR",
                iso: "fr-FR",
                name: "Français",
                file: "fr-FR.json",
            },
        ],
        defaultLocale: 'en-US',
        vueI18n: 'i18n.config.ts',
        detectBrowserLanguage: {
            useCookie: true,
            cookieKey: 'i18n_redirected',
            alwaysRedirect: true,
            fallbackLocale: 'en-US',
        }
    }
})