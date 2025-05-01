export default defineAppConfig({
    ui: {
        colors: {
            primary: 'customPrimary',
        },
        card: {
            slots: {
                root: 'rounded-lg cursor-pointer transition duration-150 ease-in-out shadow-lg shadow-primary border-1 border-primary-800 hover:shadow-xl',
                body: 'p-2! sm-p-2!',
                footer: 'px-0! border-t-1 border-t-primary-800',
            },
        }
    },
})