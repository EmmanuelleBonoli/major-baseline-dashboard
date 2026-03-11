import { api } from './api'

export const contactApi = {
    sendMessage(email: string, subject: string, body: string): Promise<void> {
        return api.post('/contact', { email, subject, body })
    }
}
