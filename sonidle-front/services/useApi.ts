import type {UseFetchOptions} from "#app";

export const useApi = async <T>(url: string, options?: UseFetchOptions<T>) => {
    const config = useRuntimeConfig()

    return useFetch<T>(`${config.public.apiBaseUrl}${url}`, {
        ...options,
        key: `${options?.method || 'GET'}:${url}`,
        watch: false,
    });
}
