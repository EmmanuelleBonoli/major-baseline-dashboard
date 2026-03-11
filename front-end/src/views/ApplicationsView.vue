<template>
  <div class="stores-view dashboard-layout">
    <!-- Header Section -->
    <div class="header-section">
      <div class="title-with-actions">
        <h2>Dashboard Global</h2>
        <div class="date-filter">
          <select v-model="selectedPeriod" class="period-select">
            <option value="7d">7 derniers jours</option>
            <option value="30d">30 derniers jours</option>
            <option value="90d">3 mois</option>
            <option value="1y">1 an</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Global Stats Overview -->
    <div class="stats-overview grid grid-3">
      <div class="stat-card glass-panel gradient-blue">
        <div class="stat-icon">
          <component :is="Download" :size="32" class="text-blue-500" />
        </div>
        <div class="stat-content">
          <h3>Total Téléchargements</h3>
          <div class="value">{{ formatNumber(globalStats.totalDownloads) }}</div>
          <div class="trend positive"><span>+12.5%</span> <small>vs période précédente</small></div>
        </div>
      </div>

      <div class="stat-card glass-panel gradient-purple">
        <div class="stat-icon">
          <component :is="Users" :size="32" class="text-purple-500" />
        </div>
        <div class="stat-content">
          <h3>Utilisateurs Actifs</h3>
          <div class="value">{{ formatNumber(globalStats.activeUsers) }}</div>
          <div class="trend positive"><span>+5.2%</span> <small>vs période précédente</small></div>
        </div>
      </div>

      <div class="stat-card glass-panel gradient-green">
        <div class="stat-icon">
          <component :is="CircleDollarSign" :size="32" class="text-green-500" />
        </div>
        <div class="stat-content">
          <h3>Revenus Totaux</h3>
          <div class="value">{{ formatCurrency(globalStats.totalRevenue) }}</div>
          <div class="trend negative"><span>-2.1%</span> <small>vs période précédente</small></div>
        </div>
      </div>
    </div>

    <!-- Global Activity Chart -->
    <div class="chart-section glass-panel">
      <h3>Activités Cumulées (Tous les jeux)</h3>
      <div class="chart-container">
        <ChartComponent v-if="!loading" :chart-data="globalChartData" color="#646cff" :height="300" />
      </div>
    </div>

    <!-- Applications List Section Removed as per user request -->
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { dashboardService } from '@/services/dashboard.service'
import ChartComponent from '@/components/Dashboard/ChartComponent.vue'
import { type ChartData, MetricType } from '@/types'
import { toast } from '@/composables/useToast'
import { Download, Users, CircleDollarSign } from 'lucide-vue-next'

const loading = ref(true)
const selectedPeriod = ref('30d')

// Mock Global Stats for demo purposes (since user has no published apps yet)
const globalStats = ref({
  totalDownloads: 125430,
  activeUsers: 45200,
  totalRevenue: 3450.5
})

const globalChartData = ref<ChartData>({
  label: 'Utilisateurs Actifs Quotidiens (Global)',
  data: []
})

onMounted(async () => {
  try {
    const { chartData, latestActiveUsers } = await dashboardService.getFormattedGlobalStats(MetricType.ACTIVE_USERS, 30)

    globalChartData.value = chartData
    globalStats.value.activeUsers = latestActiveUsers

    // const downloads = await dashboardService.getFormattedGlobalStats(MetricType.DOWNLOADS, 30)
    // const revenue = await dashboardService.getFormattedGlobalStats(MetricType.REVENUE, 30)
  } catch (error) {
    toast.error(error, 'Erreur lors du chargement des statistiques globales')
  } finally {
    loading.value = false
  }
})

const formatNumber = (num: number) => new Intl.NumberFormat('fr-FR').format(num)
const formatCurrency = (amount: number) =>
  new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(amount)
</script>

<style scoped>
.dashboard-layout {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding-bottom: 2rem;
}

.title-with-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.period-select {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  outline: none;
}

.period-select option {
  background: #2b3044;
  color: #fff;
}

.glass-panel {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

/* Stats Cards */
.gradient-blue {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1) 0%, rgba(37, 99, 235, 0.2) 100%);
  border-color: rgba(59, 130, 246, 0.3);
}
.gradient-purple {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.1) 0%, rgba(124, 58, 237, 0.2) 100%);
  border-color: rgba(139, 92, 246, 0.3);
}
.gradient-green {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.1) 0%, rgba(5, 150, 105, 0.2) 100%);
  border-color: rgba(16, 185, 129, 0.3);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.stat-icon {
  font-size: 2.5rem;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
}

.stat-content h3 {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
  margin-bottom: 0.25rem;
}

.stat-content .value {
  font-size: 1.8rem;
  font-weight: 700;
  color: #fff;
  margin-bottom: 0.25rem;
}

.trend {
  font-size: 0.85rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.trend.positive {
  color: #4ade80;
}
.trend.negative {
  color: #f87171;
}
.trend small {
  color: rgba(255, 255, 255, 0.5);
}

/* Chart Section */
.chart-section h3 {
  margin-bottom: 1.5rem;
  color: rgba(255, 255, 255, 0.9);
}

/* Apps Section */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 2rem 0 1rem;
}

.app-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 200px;
}

.app-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
}

.app-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.app-icon-wrapper {
  position: relative;
}

.app-icon {
  font-size: 2rem;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
}

.app-info h4 {
  margin: 0;
  font-size: 1.1rem;
  color: #fff;
}

.package-name {
  margin: 0;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.5);
  font-family: monospace;
}

.platforms-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
  flex-grow: 1;
}

.platform-label {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.4);
  text-transform: uppercase;
  font-weight: 600;
}

.platform-buttons {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.platform-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 0.4rem 0.8rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.85rem;
}

.platform-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.platform-btn.android:hover {
  background: rgba(61, 220, 132, 0.2);
  border-color: rgba(61, 220, 132, 0.4);
  color: #3ddc84;
}

.platform-btn.ios:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: #fff;
}

.no-platforms {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.3);
  font-style: italic;
}

.app-mini-stats {
  display: flex;
  justify-content: space-between;
  padding-top: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.mini-stat {
  display: flex;
  flex-direction: column;
}

.mini-stat .label {
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.5);
}

.mini-stat .val {
  font-size: 1rem;
  font-weight: 600;
  color: #fff;
}

.loading-state {
  text-align: center;
  padding: 4rem;
  color: rgba(255, 255, 255, 0.5);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top-color: #646cff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.btn-sm {
  padding: 0.4rem 0.8rem;
  font-size: 0.85rem;
}

.mt-2 {
  margin-top: 1rem;
}
</style>
