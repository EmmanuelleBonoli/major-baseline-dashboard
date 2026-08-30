import { api } from './api'
import { reactive } from 'vue'

// `localStorage` est indisponible pendant le pré-rendu SSG (contexte Node) :
// ce module est tiré dans le graphe via App.vue -> DashboardLayout.
const hasStorage = typeof localStorage !== 'undefined'

export const authState = reactive({
  user: hasStorage ? JSON.parse(localStorage.getItem('majorBaselineUser') || 'null') : null,
  isAuthenticated: hasStorage && !!localStorage.getItem('majorBaselineToken')
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
