
import { api } from './api'
import { reactive } from 'vue'

export const authState = reactive({
    user: JSON.parse(localStorage.getItem('majorBaselineUser') || 'null'),
    isAuthenticated: !!localStorage.getItem('majorBaselineToken')
})

export const authService = {
    async login(email: string, password: string): Promise<boolean> {
        try {
            const response = await api.post('/auth/login', { email, password })
            const { token, user } = response.data

            localStorage.setItem('majorBaselineToken', token)
            localStorage.setItem('majorBaselineUser', JSON.stringify(user))

            authState.user = user
            authState.isAuthenticated = true

            return true
        } catch (error) {
            return false
        }
    },

    logout() {
        localStorage.removeItem('majorBaselineToken')
        localStorage.removeItem('majorBaselineUser')
        authState.user = null
        authState.isAuthenticated = false
        window.location.href = '/login'
    },

    isAuthenticated(): boolean {
        return authState.isAuthenticated
    }
}
