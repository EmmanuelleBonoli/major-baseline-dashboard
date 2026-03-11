import { AxiosError } from 'axios';

export function getErrorMessage(error: unknown, fallbackMessage: string = 'Une erreur est survenue'): string {
    if (error instanceof AxiosError && error.response?.data) {
        const data = error.response.data;
        if (typeof data === 'string') {
            return data;
        }
        if (typeof data === 'object') {
            // If the backend returns a Map of validation errors (e.g., Spring Boot MethodArgumentNotValidException)
            return Object.values(data).join(' \n');
        }
    }
    if (error instanceof Error && error.message) {
        return error.message;
    }
    return fallbackMessage;
}
