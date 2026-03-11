import { dashboardApi } from './dashboard.api.service'
import type { Application, MetricType } from '@/types'

export const dashboardService = {
  // Récupérer la liste des jeux
  async getAllGames(): Promise<Application[]> {
    return dashboardApi.getGames()
  },

  // Récupérer un jeu spécifique
  async getGameById(applicationId: string): Promise<Application | null> {
    const applications = await dashboardApi.getGames()
    return applications.find(g => g.id === applicationId) || null
  },

  // Logique métier: Statistiques aggrégées (Mock)
  getMockAppStats(id: string) {
    const seed = id.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0)
    return {
      downloads: Math.floor((seed % 10000) * 1.5),
      revenue: Math.floor((seed % 5000) / 10)
    }
  },

  // Calculer les stats aggrégées pour toutes les plateformes
  getAggregatedAppStats(application: Application | null) {
    if (!application) return { downloads: 0, revenue: 0, activeUsers: 0 }
    
    return application.stores.reduce((acc, app) => {
      const stats = this.getMockAppStats(app.id)
      return {
        downloads: acc.downloads + stats.downloads,
        revenue: acc.revenue + stats.revenue,
        activeUsers: acc.activeUsers + Math.floor(stats.downloads * 0.4)
      }
    }, { downloads: 0, revenue: 0, activeUsers: 0 })
  },

  // Logique métier: Récupérer et formatter les stats globales pour le graphique
  async getFormattedGlobalStats(metricType: MetricType, days: number) {
    const endDate = new Date().toISOString().split('T')[0]
    const startDate = new Date(Date.now() - days * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
    
    const stats = await dashboardApi.getGlobalStats(metricType, startDate, endDate)
    
    return {
      chartData: {
        label: 'Utilisateurs Actifs Quotidiens (Global)',
        data: stats.map(s => ({
          date: s.date,
          value: s.value
        }))
      },
      latestActiveUsers: stats.length > 0 ? stats[stats.length - 1].value : 0
    }
  }
}
