<template>
  <div class="application-dashboard" v-if="application">
    <div class="header-section">
      <div class="application-info">
        <div class="application-icon">
          <component :is="Gamepad2" :size="40" class="text-white" />
        </div>
        <div class="title-section">
          <h2>{{ application.name }}</h2>
          <p class="subtitle">Dashboard Global - {{ application.studio || 'Studio Indépendant' }}</p>
        </div>
      </div>
      
      <div class="actions">
        <select v-model="selectedPeriod" class="period-select">
          <option value="7d">7 derniers jours</option>
          <option value="30d">30 derniers jours</option>
          <option value="90d">3 mois</option>
          <option value="1y">1 an</option>
        </select>
        <button class="btn btn-secondary flex items-center gap-2" @click="syncData">
          <component :is="RefreshCw" :size="18" /> Synchroniser
        </button>
      </div>
    </div>

    <!-- Aggregated Stats Cards -->
    <div class="stats-overview grid grid-3">
      <div class="stat-card glass-panel gradient-blue">
        <div class="stat-icon">
          <component :is="Download" :size="32" class="text-blue-500" />
        </div>
        <div class="stat-content">
          <h3>Total Téléchargements</h3>
          <div class="value">{{ formatNumber(aggregatedStats.downloads) }}</div>
          <div class="trend positive">
            <span>+{{ mockTrend() }}%</span> <small>vs période précédente</small>
          </div>
        </div>
      </div>

      <div class="stat-card glass-panel gradient-green">
        <div class="stat-icon">
          <component :is="CircleDollarSign" :size="32" class="text-green-500" />
        </div>
        <div class="stat-content">
          <h3>Revenus Totaux</h3>
          <div class="value">{{ formatCurrency(aggregatedStats.revenue) }}</div>
          <div class="trend positive">
            <span>+{{ mockTrend() }}%</span> <small>vs période précédente</small>
          </div>
        </div>
      </div>
      
      <div class="stat-card glass-panel gradient-purple">
        <div class="stat-icon">
          <component :is="Users" :size="32" class="text-purple-500" />
        </div>
        <div class="stat-content">
          <h3>Utilisateurs Actifs</h3>
          <div class="value">{{ formatNumber(aggregatedStats.activeUsers) }}</div>
          <div class="trend positive">
             <span>+{{ mockTrend() }}%</span> <small>vs période précédente</small>
          </div>
        </div>
      </div>
    </div>

    <!-- Platforms Section -->
    <div class="platforms-section glass-panel">
      <h3>Versions par Plateforme</h3>
      <div class="platforms-grid">
        <div 
          v-for="app in application.stores" 
          :key="app.id"
          class="platform-card"
          :class="app.platform === 'ANDROID' ? 'android-card' : 'ios-card'"
          @click="goToAppDashboard(app.id)"
        >
          <div class="platform-header">
            <span class="platform-icon">
              <component :is="app.platform === 'ANDROID' ? Smartphone : Monitor" :size="24" />
            </span>
            <h4>{{ app.platform === 'ANDROID' ? 'Android' : 'iOS' }}</h4>
          </div>
          <p class="package-name">{{ app.packageName || app.bundleId }}</p>
          <div class="platform-mini-stats">
            <div>
              <span class="label">DLs: </span>
              <span class="val">{{ formatNumber(dashboardService.getMockAppStats(app.id).downloads) }}</span>
            </div>
             <div>
              <span class="label">Rev: </span>
              <span class="val">{{ formatCurrency(dashboardService.getMockAppStats(app.id).revenue) }}</span>
            </div>
          </div>
          <div class="arrow">➔</div>
        </div>
      </div>
    </div>
  </div>
  <div v-else-if="loading" class="loading">
    Chargement...
  </div>
  <div v-else class="error">
    Jeu non trouvé
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { dashboardService } from '@/services/dashboard.service'
import type { Application } from '@/types'
import { toast } from '@/composables/useToast'
import { Gamepad2, Download, CircleDollarSign, Users, RefreshCw, Smartphone, Monitor } from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const application = ref<Application | null>(null)
const loading = ref(true)
const selectedPeriod = ref('30d')

const fetchGame = async () => {
  loading.value = true
  const applicationId = route.params.id as string
  try {
    application.value = await dashboardService.getGameById(applicationId)
  } catch (e) {
    toast.error(e, 'Erreur lors du chargement de l\'application')
  } finally {
    loading.value = false
  }
}

onMounted(fetchGame)

watch(() => route.params.id, fetchGame)

const aggregatedStats = computed(() => {
  return dashboardService.getAggregatedAppStats(application.value)
})

const mockTrend = () => (Math.random() * 10).toFixed(1)

const formatNumber = (num: number) => new Intl.NumberFormat('fr-FR').format(num)
const formatCurrency = (amount: number) => new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(amount)

const syncData = () => alert('Sync launched')

const goToAppDashboard = (appId: string) => {
  if (application.value) {
    router.push(`/application/${application.value.id}/store/${appId}`)
  }
}
</script>

<style scoped>
.application-dashboard {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.application-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.application-icon {
  font-size: 3rem;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.2);
}

.title-section h2 {
  font-size: 2rem;
  margin: 0;
  background: linear-gradient(90deg, #fff, #94a3b8);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  color: #94a3b8;
  margin: 0.5rem 0 0;
}

.actions {
  display: flex;
  gap: 1rem;
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
}

/* Stats Cards - Reusing styles but scoped */
.glass-panel {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
}

.gradient-blue { background: linear-gradient(135deg, rgba(59, 130, 246, 0.1) 0%, rgba(37, 99, 235, 0.2) 100%); border-color: rgba(59, 130, 246, 0.3); }
.gradient-purple { background: linear-gradient(135deg, rgba(139, 92, 246, 0.1) 0%, rgba(124, 58, 237, 0.2) 100%); border-color: rgba(139, 92, 246, 0.3); }
.gradient-green { background: linear-gradient(135deg, rgba(16, 185, 129, 0.1) 0%, rgba(5, 150, 105, 0.2) 100%); border-color: rgba(16, 185, 129, 0.3); }

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
  margin: 0 0 0.5rem;
}

.stat-content .value {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

.trend {
  font-size: 0.85rem;
  color: #4ade80;
}
.trend small { color: rgba(255, 255, 255, 0.5); }

/* Platforms Section */
.platforms-section h3 {
  margin-bottom: 1.5rem;
  color: rgba(255, 255, 255, 0.9);
}

.platforms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.platform-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.platform-card:hover {
  transform: translateY(-5px);
  border-color: rgba(255, 255, 255, 0.3);
}

.platform-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0;
  width: 4px; height: 100%;
}

.android-card::before { background: #3ddc84; }
.ios-card::before { background: #fff; }

.platform-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.platform-icon {
  font-size: 1.5rem;
}

.package-name {
  font-family: monospace;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: 1rem;
}

.platform-mini-stats {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
}

.platform-mini-stats .label {
  color: rgba(255, 255, 255, 0.5);
}

.platform-mini-stats .val {
  font-weight: 600;
}

.arrow {
  position: absolute;
  bottom: 1.5rem;
  right: 1.5rem;
  font-size: 1.2rem;
  opacity: 0.5;
  transition: opacity 0.2s, transform 0.2s;
}

.platform-card:hover .arrow {
  opacity: 1;
  transform: translateX(5px);
}
</style>
