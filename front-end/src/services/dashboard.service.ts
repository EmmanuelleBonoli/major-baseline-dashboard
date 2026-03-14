import { dashboardApi } from './dashboard.api.service'
import type { Application, AppDashboard } from '@/types'

export const dashboardService = {
  // Récupérer la liste des jeux
  async getAllGames(): Promise<Application[]> {
    return dashboardApi.getGames()
  },

  // Récupérer un jeu spécifique
  async getGameById(applicationId: string): Promise<Application | null> {
    const applications = await dashboardApi.getGames()
    return applications.find((g) => g.id === applicationId) || null
  },

  // Récupérer le dashboard complet (résumé + charts) pour une application
  async getApplicationDashboard(applicationId: string, days: number): Promise<AppDashboard> {
    const endDate = new Date().toISOString().split('T')[0]
    const startDate = new Date(Date.now() - days * 24 * 60 * 60 * 1000).toISOString().split('T')[0]

    return dashboardApi.getApplicationDashboard(applicationId, startDate, endDate)
  },

  // Récupérer le dashboard complet pour un store spécifique
  async getStoreDashboard(storeId: string, days: number): Promise<AppDashboard> {
    const endDate = new Date().toISOString().split('T')[0]
    const startDate = new Date(Date.now() - days * 24 * 60 * 60 * 1000).toISOString().split('T')[0]

    return dashboardApi.getAppDashboard(storeId, startDate, endDate)
  },

  // Récupérer le dashboard global
  async getGlobalDashboard(days: number): Promise<AppDashboard> {
    const endDate = new Date().toISOString().split('T')[0]
    const startDate = new Date(Date.now() - days * 24 * 60 * 60 * 1000).toISOString().split('T')[0]

    return dashboardApi.getGlobalSummary(startDate, endDate)
  },

  // Créer une application
  async createApplication(name: string, icon: string): Promise<Application> {
    return dashboardApi.createGame({ name, icon })
  },

  // Supprimer une application
  async deleteApplication(id: string): Promise<void> {
    return dashboardApi.deleteGame(id)
  },

  // Restaurer une application
  async restoreApplication(id: string): Promise<void> {
    return dashboardApi.restoreGame(id)
  },

  // Ajouter un store
  async addStore(appId: string, storeData: any): Promise<any> {
    return dashboardApi.addStore(appId, storeData)
  },

  // Synchronisation globale
  async syncAll(): Promise<void> {
    return dashboardApi.syncAll()
  },

  // Supprimer un store
  async deleteStore(id: string): Promise<void> {
    return dashboardApi.deleteStore(id)
  }
}
