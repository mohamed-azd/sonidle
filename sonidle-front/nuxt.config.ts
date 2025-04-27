// https://nuxt.com/docs/api/configuration/nuxt-config

export default defineNuxtConfig({
    compatibilityDate: '2024-11-01',
    devtools: {enabled: true},
    modules: ['@nuxt/icon', '@nuxt/ui', '@nuxt/fonts', '@nuxt/image', '@nuxtjs/i18n', '@pinia/nuxt', 'pinia-plugin-persistedstate/nuxt'],
    css: ['assets/css/main.css'],
    app: {
        head: {
            link: [{ rel: 'icon', type: 'image/png', href: 'logo_without_text.png' }],
            script: [
                {
                    innerHTML: 'window.global = window;',
                    type: 'text/javascript',
                    tagPosition: 'head',
                }
            ]
        }
    },
    pinia: {
        storesDirs: ['./stores/**'],
    },
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
    },
    runtimeConfig: {
        public: {
            apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL,
            wsUrl: process.env.NUXT_PUBLIC_WS_URL,
        }
    }
})