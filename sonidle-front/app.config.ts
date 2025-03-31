export default defineAppConfig({
    ui: {
        colors: {
            primary: 'customPrimary',
        },
        card: {
            slots: {
                root: 'rounded-lg cursor-pointer',
                body: 'p-0! sm-p-0!',
            },
        }
    },
})