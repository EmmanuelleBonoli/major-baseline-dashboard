import { defineStore } from 'pinia'
import { ref } from 'vue'
import { dashboardService } from '@/services/dashboard.service'
import type { Application } from '@/types'

export const useDashboardStore = defineStore('dashboard', () => {
  // State
  const applications = ref<Application[]>([])
  const selectedPeriod = ref('30d')
  const loading = ref(false)
  const isFetchingApps = ref(false)

  // Caches pour éviter de requêter ce qu'on a déjà
  const globalStatsCache = ref<Record<string, any>>({})
  const appStatsCache = ref<Record<string, any>>({})

  const periodsDaysMap: Record<string, number> = {
    '7d': 7,
    '30d': 30,
    '90d': 90,
    '1y': 365
  }

  // Getters
  const getApplicationById = (id: string) => {
    return applications.value.find((app) => app.id === id) || null
  }

  // Actions
  async function fetchApplications() {
    if (isFetchingApps.value) return
    if (applications.value.length > 0) return

    isFetchingApps.value = true
    try {
      const apps = await dashboardService.getAllGames()
      applications.value = apps
    } catch (error) {
      console.error('Failed to fetch applications:', error)
    } finally {
      isFetchingApps.value = false
    }
  }

  async function fetchApplicationById(id: string) {
    const existing = getApplicationById(id)
    if (existing) return existing

    try {
      const app = await dashboardService.getGameById(id)
      if (app && !applications.value.find((a) => a.id === id)) {
        applications.value.push(app)
      }
      return app
    } catch (error) {
      console.error(`Failed to fetch application ${id}:`, error)
      return null
    }
  }

  async function fetchGlobalStats(period: string) {
    const cacheKey = period
    if (globalStatsCache.value[cacheKey]) {
      return globalStatsCache.value[cacheKey]
    }

    loading.value = true
    try {
      const days = periodsDaysMap[period] || 30
      const dashboard = await dashboardService.getGlobalDashboard(days)

      // On cherche les charts par défaut (Active Users ou le premier dispo)
      const activeUsersChart =
        dashboard.charts.find((c) => c.label.toLowerCase().includes('actifs')) || dashboard.charts[0]

      const data = {
        chartData: activeUsersChart,
        summary: dashboard.summary
      }

      globalStatsCache.value[cacheKey] = data
      return data
    } finally {
      loading.value = false
    }
  }

  async function fetchApplicationStats(appId: string, period: string) {
    const cacheKey = `${appId}_${period}`
    if (appStatsCache.value[cacheKey]) {
      return appStatsCache.value[cacheKey]
    }

    loading.value = true
    try {
      const days = periodsDaysMap[period] || 30
      const dashboard = await dashboardService.getApplicationDashboard(appId, days)

      const stats = {
        summary: dashboard.summary,
        charts: dashboard.charts,
        storeSummaries: dashboard.storeSummaries
      }

      appStatsCache.value[cacheKey] = stats
      return stats
    } finally {
      loading.value = false
    }
  }

  async function fetchStoreStats(storeId: string, period: string) {
    const cacheKey = `store_${storeId}_${period}`
    if (appStatsCache.value[cacheKey]) {
      return appStatsCache.value[cacheKey]
    }

    loading.value = true
    try {
      const days = periodsDaysMap[period] || 30
      const dashboard = await dashboardService.getStoreDashboard(storeId, days)

      const stats = {
        summary: dashboard.summary,
        charts: dashboard.charts
      }

      appStatsCache.value[cacheKey] = stats
      return stats
    } finally {
      loading.value = false
    }
  }

  function setPeriod(period: string) {
    selectedPeriod.value = period
  }

  async function addApplication(name: string, icon: string) {
    const newApp = await dashboardService.createApplication(name, icon)
    applications.value.push(newApp)
    return newApp
  }

  async function deleteApplication(id: string) {
    await dashboardService.deleteApplication(id)
    const app = getApplicationById(id)
    if (app) app.active = false
  }

  async function restoreApplication(id: string) {
    await dashboardService.restoreApplication(id)
    const app = getApplicationById(id)
    if (app) app.active = true
  }

  async function addStore(appId: string, storeData: any) {
    const newStore = await dashboardService.addStore(appId, storeData)
    const app = getApplicationById(appId)
    if (app) {
      if (!app.stores) app.stores = []
      app.stores.push(newStore)
    }
    return newStore
  }

  async function deleteStore(appId: string, storeId: string) {
    await dashboardService.deleteStore(storeId)
    const app = getApplicationById(appId)
    if (app) {
      // Find the store and set it to inactive locally
      const store = app.stores.find((s) => s.id === storeId)
      if (store) {
        store.active = false
      }
    }
  }

  function clearStatsCache() {
    globalStatsCache.value = {}
    appStatsCache.value = {}
  }

  return {
    applications,
    selectedPeriod,
    loading,
    getApplicationById,
    fetchApplications,
    fetchApplicationById,
    fetchGlobalStats,
    fetchApplicationStats,
    fetchStoreStats,
    clearStatsCache,
    setPeriod,
    addApplication,
    deleteApplication,
    restoreApplication,
    addStore,
    deleteStore
  }
})
