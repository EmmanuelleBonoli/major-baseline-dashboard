<template>
  <div class="stores-view dashboard-layout">
    <!-- Header Section -->
    <div class="header-section flex flex-col lg:flex-row lg:items-center justify-between gap-6 mb-8">
      <div class="application-info flex items-center gap-4 md:gap-6">
        <div
          class="application-icon shrink-0 text-3xl md:text-4xl bg-white/5 w-12 h-12 md:w-16 md:h-16 flex items-center justify-center rounded-2xl shadow-lg border border-white/10"
        >
          🌍
        </div>
        <div class="title-section min-w-0">
          <h2 class="text-xl md:text-3xl font-bold truncate">Dashboard Global</h2>
        </div>
      </div>

      <div class="actions flex flex-wrap items-center gap-3 md:gap-4">
        <button class="btn-primary flex items-center gap-2" @click="showAddAppModal = true">
          <component :is="Plus" :size="18" />
          <span>Nouveau Projet</span>
        </button>
        <button class="btn-ghost flex items-center gap-2" @click="handleSyncAll" :disabled="isSyncing">
          <component :is="RefreshCw" :size="18" :class="{ 'animate-spin': isSyncing }" />
          <span>Actualiser</span>
        </button>
        <div class="date-filter">
          <AppSelect v-model="selectedPeriod" :options="periodOptions" />
        </div>
      </div>
    </div>

    <Modal :show="showAddAppModal" title="Ajouter une application" @close="showAddAppModal = false">
      <div class="form-group mb-4">
        <label>Nom de l'application</label>
        <input v-model="newApp.name" type="text" placeholder="Ex: My Super Game" class="form-input" />
      </div>
      <div class="form-group mb-4">
        <label>Icône (Emoji)</label>
        <input v-model="newApp.icon" type="text" placeholder="Ex: 🎮" class="form-input" />
      </div>
      <template #footer>
        <button class="btn-ghost" @click="showAddAppModal = false">Annuler</button>
        <button class="btn-primary" @click="handleAddApp" :disabled="!newApp.name">Créer</button>
      </template>
    </Modal>

    <!-- Global Stats Overview -->
    <div v-if="globalSummary" class="stats-overview flex flex-wrap gap-6">
      <div class="stat-card glass-panel gradient-blue">
        <div class="stat-icon">
          <component :is="Download" :size="32" class="text-blue-500" />
        </div>
        <div class="stat-content">
          <h3>Total Téléchargements</h3>
          <div class="value">{{ formatNumber(globalSummary.totalDownloads) }}</div>
          <div class="trend" :class="globalSummary.downloadsVariation >= 0 ? 'positive' : 'negative'">
            <component :is="globalSummary.downloadsVariation >= 0 ? ArrowUpRight : ArrowDownRight" :size="16" />
            <span>{{ Math.abs(globalSummary.downloadsVariation).toFixed(1) }}%</span>
            <small>vs période précédente</small>
          </div>
        </div>
      </div>

      <div class="stat-card glass-panel gradient-purple">
        <div class="stat-icon">
          <component :is="Users" :size="32" class="text-purple-500" />
        </div>
        <div class="stat-content">
          <h3>Utilisateurs Actifs</h3>
          <div class="value">{{ formatNumber(globalSummary.activeUsers) }}</div>
          <div class="trend" :class="globalSummary.activeUsersVariation >= 0 ? 'positive' : 'negative'">
            <component :is="globalSummary.activeUsersVariation >= 0 ? ArrowUpRight : ArrowDownRight" :size="16" />
            <span>{{ Math.abs(globalSummary.activeUsersVariation).toFixed(1) }}%</span>
            <small>vs période précédente</small>
          </div>
        </div>
      </div>

      <div class="stat-card glass-panel gradient-green">
        <div class="stat-icon">
          <component :is="CircleDollarSign" :size="32" class="text-green-500" />
        </div>
        <div class="stat-content">
          <h3>Revenus Totaux</h3>
          <div class="value">{{ formatCurrency(globalSummary.totalRevenue) }}</div>
          <div class="trend" :class="globalSummary.revenueVariation >= 0 ? 'positive' : 'negative'">
            <component :is="globalSummary.revenueVariation >= 0 ? ArrowUpRight : ArrowDownRight" :size="16" />
            <span>{{ Math.abs(globalSummary.revenueVariation).toFixed(1) }}%</span>
            <small>vs période précédente</small>
          </div>
        </div>
      </div>

      <div
        class="stat-card glass-panel"
        style="
          background: linear-gradient(135deg, rgba(239, 68, 68, 0.1) 0%, rgba(220, 38, 38, 0.2) 100%);
          border-color: rgba(239, 68, 68, 0.3);
        "
      >
        <div class="stat-icon">
          <component :is="AlertTriangle" :size="32" class="text-red-500" />
        </div>
        <div class="stat-content">
          <h3>Crashes</h3>
          <div class="value">{{ formatNumber(globalSummary.totalCrashes || 0) }}</div>
          <div class="trend" :class="(globalSummary.crashesVariation || 0) <= 0 ? 'positive' : 'negative'">
            <component :is="(globalSummary.crashesVariation || 0) <= 0 ? ArrowDownRight : ArrowUpRight" :size="16" />
            <span>{{ Math.abs(globalSummary.crashesVariation || 0).toFixed(1) }}%</span>
            <small>vs période précédente</small>
          </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { dashboardService } from '@/services/dashboard.service'
import { useDashboardStore } from '@/stores/useDashboardStore'
import AppSelect from '@/components/Dashboard/ui/AppSelect.vue'
import ChartComponent from '@/components/Dashboard/ChartComponent.vue'
import Modal from '@/components/Dashboard/ui/Modal.vue'
import { type ChartData, type Summary } from '@/types'
import { toast } from '@/composables/useToast'
import {
  Download,
  Users,
  CircleDollarSign,
  ArrowUpRight,
  ArrowDownRight,
  Plus,
  RefreshCw,
  AlertTriangle
} from 'lucide-vue-next'

const store = useDashboardStore()
const loading = ref(true)
const isSyncing = ref(false)
const showAddAppModal = ref(false)
const newApp = ref({ name: '', icon: '🎮' })

const selectedPeriod = computed({
  get: () => store.selectedPeriod,
  set: (val) => store.setPeriod(val)
})

const periodOptions = [
  { value: '7d', label: '7 derniers jours' },
  { value: '30d', label: '30 derniers jours' },
  { value: '90d', label: '3 mois' },
  { value: '1y', label: '1 an' }
]

const globalSummary = ref<Summary | null>(null)
const globalChartData = ref<ChartData>({
  label: 'Utilisateurs Actifs Quotidiens (Global)',
  data: []
})

const fetchGlobalData = async () => {
  loading.value = true
  try {
    const data = await store.fetchGlobalStats(selectedPeriod.value)
    globalChartData.value = data.chartData
    globalSummary.value = data.summary
  } catch (error) {
    toast.error(error, 'Erreur lors du chargement des statistiques globales')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchGlobalData()
})

// Surveiller le changement de période pour rafraîchir les données
watch(selectedPeriod, fetchGlobalData)

const handleAddApp = async () => {
  if (!newApp.value.name) return
  try {
    await store.addApplication(newApp.value.name, newApp.value.icon)
    toast.success('Application créée avec succès')
    showAddAppModal.value = false
    newApp.value = { name: '', icon: '🎮' }
  } catch (e) {
    toast.error(e, 'Erreur lors de la création')
  }
}

const handleSyncAll = async () => {
  isSyncing.value = true
  toast.info('Actualisation globale des données...')
  try {
    await dashboardService.syncAll()
    store.clearStatsCache() // On vide le cache car le back a de nouvelles données
    await fetchGlobalData()
    toast.success('Toutes les données sont à jour')
  } catch (e) {
    toast.error(e, 'Échec de la synchronisation')
  } finally {
    isSyncing.value = false
  }
}

const formatNumber = (num: number) => new Intl.NumberFormat('fr-FR').format(num)
const formatCurrency = (amount: number) => {
  const currency = globalSummary.value?.currency || 'EUR'
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency }).format(amount)
}
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

.date-filter {
  display: flex;
  align-items: center;
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
  flex: 1;
  min-width: 300px;
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
