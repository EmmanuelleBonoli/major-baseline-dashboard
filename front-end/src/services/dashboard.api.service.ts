import { api } from './api'
import type { Store, AppDashboard, Stats, MetricType, Application } from '@/types'

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
  getAppDashboard(appId: string, startDate?: string, endDate?: string): Promise<AppDashboard> {
    const params: any = {}
    if (startDate) params.startDate = startDate
    if (endDate) params.endDate = endDate

    return api.get(`/dashboard/stores/${appId}`, { params }).then((res) => res.data)
  },

  // Récupérer les stats globales
  getGlobalStats(metricType: MetricType, startDate: string, endDate: string): Promise<Stats[]> {
    return api
      .get('/dashboard/global-stats', {
        params: { metricType, startDate, endDate }
      })
      .then((res) => res.data)
  },

  // Récupérer des stats spécifiques
  getStats(appId: string, metricType: MetricType, startDate: string, endDate: string): Promise<Stats[]> {
    return api
      .get(`/dashboard/stores/${appId}/stats`, {
        params: { metricType, startDate, endDate }
      })
      .then((res) => res.data)
  },

  // Synchroniser les données du store
  syncStoreData(appId: string): Promise<void> {
    return api.post(`/dashboard/stores/${appId}/sync`)
  }
}
