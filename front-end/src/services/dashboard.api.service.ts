import { api } from './api'
import type { Store, AppDashboard, Application } from '@/types'

export const dashboardApi = {
  // Récupérer toutes les stores
  getStores(): Promise<Store[]> {
    return api.get('/dashboard/stores').then((res) => res.data)
  },

  // Récupérer tous les jeux
  getGames(): Promise<Application[]> {
    return api.get('/dashboard/applications').then((res) => res.data)
  },

  // Récupérer le dashboard d'une store
  getAppDashboard(storeId: string, startDate?: string, endDate?: string): Promise<AppDashboard> {
    const params: any = {}
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate

    return api.get(`/dashboard/stores/${storeId}`, { params }).then((res) => res.data)
  },

  // Récupérer le dashboard d'une application (agrégé)
  getApplicationDashboard(appId: string, startDate?: string, endDate?: string): Promise<AppDashboard> {
    const params: any = {}
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate

    return api.get(`/dashboard/applications/${appId}/stats`, { params }).then((res) => res.data)
  },

  // Récupérer le dashboard global (agrégé)
  getGlobalSummary(startDate?: string, endDate?: string): Promise<AppDashboard> {
    const params: any = {}
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate

    return api.get('/dashboard/global-summary', { params }).then((res) => res.data)
  },

  // Créer un nouveau jeu
  createGame(game: Partial<Application>): Promise<Application> {
    return api.post('/dashboard/applications', game).then((res) => res.data)
  },

  // Supprimer un jeu (cascade)
  deleteGame(id: string): Promise<void> {
    return api.delete(`/dashboard/applications/${id}`)
  },

  // Restaurer un jeu archivé
  restoreGame(id: string): Promise<void> {
    return api.post(`/dashboard/applications/${id}/restore`)
  },

  // Synchronisation globale (tous les projets actifs)
  syncAll(): Promise<void> {
    return api.post('/dashboard/sync-all')
  },

  // Ajouter un store à un jeu
  addStore(appId: string, store: Partial<Store>): Promise<Store> {
    return api.post(`/dashboard/applications/${appId}/stores`, store).then((res) => res.data)
  },

  // Supprimer un store (soft delete)
  deleteStore(id: string): Promise<void> {
    return api.delete(`/dashboard/stores/${id}`)
  }
}
