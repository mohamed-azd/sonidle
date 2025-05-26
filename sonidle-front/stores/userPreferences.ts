export const useUserPreferencesStore = defineStore("userPreferences", () => {
    const volume = ref(100);

    return {volume}
}, {persist: true});