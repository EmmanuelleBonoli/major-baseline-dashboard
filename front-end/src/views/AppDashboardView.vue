<template>
  <div class="application-dashboard" v-if="application">
    <div v-if="application.active === false" class="archive-banner mb-6">
      <div class="flex items-center gap-3">
        <component :is="Archive" :size="20" />
        <span
          >Ce projet est actuellement <strong>archivé</strong>. Ses données statistiques sont conservées mais il est
          masqué de votre liste de projets actifs.</span
        >
      </div>
      <button class="btn-primary btn-sm" @click="handleRestoreApp">Désarchiver le projet</button>
    </div>

    <div class="header-section flex flex-col lg:flex-row lg:items-center justify-between gap-6 mb-8">
      <div class="application-info flex items-center gap-4 md:gap-6">
        <button
          v-if="route.params.storeId"
          class="btn-ghost p-2"
          @click="router.push(`/dashboard/application/${application.id}`)"
        >
          <component :is="ArrowLeft" :size="20" />
        </button>
        <div
          class="application-icon shrink-0 text-3xl md:text-4xl bg-white/5 w-12 h-12 md:w-16 md:h-16 flex items-center justify-center rounded-2xl shadow-lg border border-white/10"
        >
          {{ application.icon || '🎮' }}
        </div>
        <div class="title-section min-w-0 flex items-center gap-3">
          <h2 class="text-xl md:text-3xl font-bold truncate">
            {{
              route.params.storeId
                ? application.stores.find((s) => s.id === route.params.storeId)?.platform === 'ANDROID'
                  ? 'Android'
                  : 'iOS'
                : application.name
            }}
          </h2>
          <span v-if="application.active === false" class="badge-archive">Archivé</span>
        </div>
      </div>

      <div class="actions flex flex-wrap items-center gap-3 md:gap-4">
        <AppSelect v-model="selectedPeriod" :options="periodOptions" class="flex-1 sm:flex-none" />
        <button
          v-if="application.active !== false"
          class="btn-delete flex-1 sm:flex-none flex items-center justify-center gap-2"
          title="Archiver le projet"
          @click="handleDeleteApp"
        >
          <component :is="Archive" :size="18" />
        </button>
      </div>
    </div>

    <!-- Aggregated Stats Cards -->
    <div v-if="applicationStats.summary" class="stats-overview flex flex-wrap gap-6">
      <div class="stat-card glass-panel gradient-blue">
        <div class="stat-icon">
          <component :is="Download" :size="32" class="text-blue-500" />
        </div>
        <div class="stat-content">
          <h3>Total Téléchargements</h3>
          <div class="value">{{ formatNumber(applicationStats.summary.totalDownloads) }}</div>
          <div class="trend" :class="applicationStats.summary.downloadsVariation >= 0 ? 'positive' : 'negative'">
            <component
              :is="applicationStats.summary.downloadsVariation >= 0 ? ArrowUpRight : ArrowDownRight"
              :size="16"
            />
            <span>{{ Math.abs(applicationStats.summary.downloadsVariation).toFixed(1) }}%</span>
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
          <div class="value">{{ formatCurrency(applicationStats.summary.totalRevenue) }}</div>
          <div class="trend" :class="applicationStats.summary.revenueVariation >= 0 ? 'positive' : 'negative'">
            <component
              :is="applicationStats.summary.revenueVariation >= 0 ? ArrowUpRight : ArrowDownRight"
              :size="16"
            />
            <span>{{ Math.abs(applicationStats.summary.revenueVariation).toFixed(1) }}%</span>
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
          <div class="value">{{ formatNumber(applicationStats.summary.activeUsers) }}</div>
          <div class="trend" :class="applicationStats.summary.activeUsersVariation >= 0 ? 'positive' : 'negative'">
            <component
              :is="applicationStats.summary.activeUsersVariation >= 0 ? ArrowUpRight : ArrowDownRight"
              :size="16"
            />
            <span>{{ Math.abs(applicationStats.summary.activeUsersVariation).toFixed(1) }}%</span>
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
          <div class="value">{{ formatNumber(applicationStats.summary.totalCrashes || 0) }}</div>
          <div class="trend" :class="(applicationStats.summary.crashesVariation || 0) <= 0 ? 'positive' : 'negative'">
            <component
              :is="(applicationStats.summary.crashesVariation || 0) <= 0 ? ArrowDownRight : ArrowUpRight"
              :size="16"
            />
            <span>{{ Math.abs(applicationStats.summary.crashesVariation || 0).toFixed(1) }}%</span>
            <small>vs période précédente</small>
          </div>
        </div>
      </div>
    </div>

    <div v-if="applicationStats.charts && applicationStats.charts.length > 0" class="charts-section">
      <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
        <div v-for="chart in applicationStats.charts" :key="chart.label" class="chart-card glass-panel">
          <h3 class="mb-4 text-sm font-semibold opacity-70">{{ chart.label }}</h3>
          <ChartComponent :chart-data="chart" :color="getChartColor(chart.label)" :height="250" />
        </div>
      </div>
    </div>

    <div v-if="!route.params.storeId" class="platforms-section glass-panel">
      <h3>Versions par Plateforme</h3>
      <div class="platforms-grid">
        <div
          v-for="app in application.stores"
          :key="app.id"
          :class="[
            'platform-card',
            app.platform === 'ANDROID' ? 'android-card' : 'ios-card',
            { 'is-inactive': application.active === false || app.active === false }
          ]"
        >
          <div v-if="app.active !== false" class="delete-store-btn" @click.stop="handleDeleteStore(app.id)">
            <component :is="X" :size="16" />
          </div>
          <div @click="goToAppDashboard(app.id)">
            <div class="platform-header">
              <span class="platform-icon">
                <component :is="app.platform === 'ANDROID' ? Smartphone : Monitor" :size="24" />
              </span>
              <div class="flex flex-col">
                <div class="flex items-center gap-2">
                  <h4>{{ app.platform === 'ANDROID' ? 'Android' : 'iOS' }}</h4>
                  <div
                    v-if="getStoreSummary(app.id).averageRating"
                    class="flex items-center gap-1 text-xs text-yellow-500"
                  >
                    <component :is="Star" :size="12" fill="currentColor" />
                    <span>{{ getStoreSummary(app.id).averageRating.toFixed(1) }}</span>
                  </div>
                </div>
                <span v-if="application.active === false || app.active === false" class="inactive-badge">Inactif</span>
              </div>
            </div>
            <p class="package-name truncate">{{ app.packageName || app.bundleId }}</p>
            <div class="platform-mini-stats">
              <div>
                <span class="label">DLs: </span>
                <span class="val">{{ formatNumber(getStoreSummary(app.id).downloads) }}</span>
              </div>
              <div>
                <span class="label">Rev: </span>
                <span class="val">{{ formatCurrency(getStoreSummary(app.id).revenue) }}</span>
              </div>
            </div>
            <div class="arrow">➔</div>
          </div>
        </div>

        <div v-if="application.active !== false" class="platform-card add-store-card" @click="showAddStoreModal = true">
          <div class="add-icon">
            <component :is="Plus" :size="32" />
          </div>
          <p>Ajouter un store</p>
        </div>
      </div>
    </div>

    <Modal :show="showAddStoreModal" title="Ajouter un store" @close="showAddStoreModal = false">
      <div class="form-group mb-6">
        <AppSelect v-model="newStore.platform" label="Plateforme" :options="platformOptions">
          <template #prefix="{ selected }">
            <component :is="selected?.value === 'ANDROID' ? Smartphone : Monitor" :size="18" class="text-teal" />
          </template>
        </AppSelect>
      </div>

      <div v-if="newStore.platform === 'ANDROID'" class="form-group mb-4">
        <label>Package Name</label>
        <input v-model="newStore.packageName" type="text" placeholder="com.example.game" class="form-input" />
      </div>

      <div v-if="newStore.platform === 'IOS'" class="flex flex-col gap-4">
        <div class="form-group">
          <label>Bundle ID</label>
          <input v-model="newStore.bundleId" type="text" placeholder="com.example.game.ios" class="form-input" />
        </div>
        <div class="form-group">
          <label>App Store ID</label>
          <input v-model="newStore.appStoreId" type="text" placeholder="123456789" class="form-input" />
        </div>
      </div>

      <template #footer>
        <button class="btn-ghost" @click="showAddStoreModal = false">Annuler</button>
        <button class="btn-primary" @click="handleAddStore" :disabled="!isNewStoreValid">Ajouter</button>
      </template>
    </Modal>

    <ConfirmModal
      :show="showConfirmArchive"
      title="Archiver le projet"
      :message="`Voulez-vous vraiment archiver l'application '${application.name}' ? Ses données seront conservées mais elle sera masquée de vos projets actifs.`"
      confirm-text="Archiver"
      @confirm="executeArchiveApp"
      @cancel="showConfirmArchive = false"
    />

    <ConfirmModal
      :show="showConfirmDeleteStore"
      title="Masquer le store"
      message="Voulez-vous masquer ce store ? Les statistiques historiques seront conservées pour les totaux."
      confirm-text="Masquer"
      @confirm="executeDeleteStore"
      @cancel="showConfirmDeleteStore = false"
    />
  </div>
  <div v-else-if="loading" class="loading">Chargement...</div>
  <div v-else class="error">Jeu non trouvé</div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useDashboardStore } from '@/stores/useDashboardStore'
import Modal from '@/components/Dashboard/ui/Modal.vue'
import ConfirmModal from '@/components/Dashboard/ui/ConfirmModal.vue'
import AppSelect from '@/components/Dashboard/ui/AppSelect.vue'
import ChartComponent from '@/components/Dashboard/ChartComponent.vue'
import type { Application } from '@/types'
import { toast } from '@/composables/useToast'
import {
  Download,
  CircleDollarSign,
  Users,
  Smartphone,
  Monitor,
  ArrowUpRight,
  ArrowDownRight,
  Archive,
  Plus,
  X,
  Star,
  ArrowLeft,
  AlertTriangle
} from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const store = useDashboardStore()

const application = ref<Application | null>(null)
const loading = ref(true)
const applicationStats = ref<any>({ summary: null, charts: [], storeSummaries: [] })
const showAddStoreModal = ref(false)
const showConfirmArchive = ref(false)
const showConfirmDeleteStore = ref(false)
const storeIdToDelete = ref<string | null>(null)
const newStore = ref({ platform: 'ANDROID', packageName: '', bundleId: '', appStoreId: '' })

const isNewStoreValid = computed(() => {
  if (newStore.value.platform === 'ANDROID') return !!newStore.value.packageName
  return !!newStore.value.bundleId
})

const platformOptions = [
  { label: 'Google Play Store (Android)', value: 'ANDROID' },
  { label: 'Apple App Store (iOS)', value: 'IOS' }
]

const periodOptions = [
  { value: '7d', label: '7 derniers jours' },
  { value: '30d', label: '30 derniers jours' },
  { value: '90d', label: '3 mois' },
  { value: '1y', label: '1 an' }
]

const selectedPeriod = computed({
  get: () => store.selectedPeriod,
  set: (val) => store.setPeriod(val)
})

const fetchGameData = async () => {
  const applicationId = route.params.id as string
  const storeId = route.params.storeId as string
  if (!applicationId) return

  loading.value = true
  try {
    // 1. Fetch l'application si pas déjà là
    application.value = await store.fetchApplicationById(applicationId)

    // 2. Fetch les stats (via cache store)
    if (application.value) {
      if (storeId) {
        applicationStats.value = await store.fetchStoreStats(storeId, selectedPeriod.value)
      } else {
        applicationStats.value = await store.fetchApplicationStats(applicationId, selectedPeriod.value)
      }
    }
  } catch (e) {
    toast.error(e, 'Erreur lors du chargement des données')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchGameData()
})

watch(() => route.params.id, fetchGameData)
watch(() => route.params.storeId, fetchGameData)
watch(selectedPeriod, fetchGameData)

const getStoreSummary = (storeId: string) => {
  return (
    applicationStats.value.storeSummaries?.find((s: any) => s.storeId === storeId) || {
      downloads: 0,
      revenue: 0,
      averageRating: 0
    }
  )
}

const getChartColor = (label: string) => {
  const l = label.toLowerCase()
  if (l.includes('téléchargement')) return '#3b82f6'
  if (l.includes('revenu')) return '#10b981'
  if (l.includes('actif')) return '#8b5cf6'
  if (l.includes('crash')) return '#ef4444'
  return '#646cff'
}

const formatNumber = (num: number) => new Intl.NumberFormat('fr-FR').format(num)
const formatCurrency = (amount: number) => {
  const currency = applicationStats.value.summary?.currency || 'EUR'
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency }).format(amount)
}

const goToAppDashboard = (appId: string) => {
  if (application.value) {
    router.push(`/dashboard/application/${application.value.id}/store/${appId}`)
  }
}

const handleDeleteApp = () => {
  showConfirmArchive.value = true
}

const executeArchiveApp = async () => {
  if (!application.value) return
  showConfirmArchive.value = false
  try {
    await store.deleteApplication(application.value.id)
    toast.success('Application archivée')
  } catch (e) {
    toast.error(e, "Erreur lors de l'archivage")
  }
}

const handleRestoreApp = async () => {
  if (!application.value) return
  try {
    await store.restoreApplication(application.value.id)
    toast.success('Projet restauré')
  } catch (e) {
    toast.error(e, 'Erreur lors de la restauration')
  }
}

const handleDeleteStore = (storeId: string) => {
  storeIdToDelete.value = storeId
  showConfirmDeleteStore.value = true
}

const executeDeleteStore = async () => {
  if (!application.value || !storeIdToDelete.value) return
  showConfirmDeleteStore.value = false
  try {
    await store.deleteStore(application.value.id, storeIdToDelete.value)
    toast.success('Store masqué')
  } catch (e) {
    toast.error(e, 'Erreur lors du masquage')
  } finally {
    storeIdToDelete.value = null
  }
}

const handleAddStore = async () => {
  if (!application.value) return
  try {
    await store.addStore(application.value.id, newStore.value)
    toast.success('Store ajouté avec succès')
    showAddStoreModal.value = false
    newStore.value = { platform: 'ANDROID', packageName: '', bundleId: '', appStoreId: '' }
  } catch (e) {
    toast.error(e, "Erreur lors de l'ajout")
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
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
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
  appearance: none;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 0.6rem 2.5rem 0.6rem 1rem;
  border-radius: 10px;
  cursor: pointer;
  outline: none;
  font-weight: 500;
  transition: all 0.2s;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='rgba(255,255,255,0.5)'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='top m19 9l-7 7-7-7'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 1rem;
}

.period-select:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
}

.btn-sync {
  background: var(--color-teal);
  color: white;
  border: none;
  height: 48px;
  padding: 0 1.5rem;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(26, 139, 157, 0.2);
}

.btn-sync:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(26, 139, 157, 0.4);
  filter: brightness(1.1);
}

.btn-sync:active {
  transform: translateY(0);
}

.btn-delete {
  background: rgba(248, 113, 113, 0.1);
  color: #f87171;
  border: 1px solid rgba(248, 113, 113, 0.2);
  height: 48px;
  width: 48px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-delete:hover {
  background: #f87171;
  color: white;
  transform: scale(1.05);
}

.archive-banner {
  background: linear-gradient(90deg, rgba(212, 175, 55, 0.1) 0%, rgba(212, 175, 55, 0.2) 100%);
  border: 1px solid rgba(212, 175, 55, 0.3);
  padding: 1rem 1.5rem;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
}

.archive-banner span {
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.8);
}

.archive-banner strong {
  color: var(--color-gold);
}

.badge-archive {
  background: rgba(212, 175, 55, 0.1);
  color: var(--color-gold);
  border: 1px solid rgba(212, 175, 55, 0.3);
  padding: 0.2rem 0.6rem;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.is-inactive {
  opacity: 0.5;
  filter: grayscale(0.5);
  pointer-events: auto; /* Permet quand même d'aller voir le détail si on veut */
}

.inactive-badge {
  font-size: 0.65rem;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.1rem 0.4rem;
  border-radius: 4px;
  text-transform: uppercase;
  width: fit-content;
  color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.period-select option {
  background: #1b1e2b;
}

/* Stats Cards - Reusing styles but scoped */
.stat-card .trend span {
  margin-right: 0.4rem;
}

.stat-card .trend small {
  color: rgba(255, 255, 255, 0.5);
  white-space: nowrap;
}

.glass-panel {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
}

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
.trend span {
  margin-right: 0.5rem;
}

.trend small {
  color: rgba(255, 255, 255, 0.5);
  white-space: nowrap;
}

/* Platforms Section */
.platforms-section h3 {
  margin-bottom: 1.5rem;
  color: rgba(255, 255, 255, 0.9);
}

.platforms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
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
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
}

.android-card::before {
  background: #3ddc84;
}
.ios-card::before {
  background: #fff;
}

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
  transition:
    opacity 0.2s,
    transform 0.2s;
}

.platform-card:hover .arrow {
  opacity: 1;
  transform: translateX(5px);
}

.delete-store-btn {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: rgba(248, 113, 113, 0.1);
  color: #f87171;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  opacity: 0;
  transition: all 0.2s;
}

.platform-card:hover .delete-store-btn {
  opacity: 1;
}

.delete-store-btn:hover {
  background: #f87171;
  color: white;
  transform: scale(1.1);
}

.add-store-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  border: 2px dashed rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.02);
  min-height: 180px;
}

.add-store-card:hover {
  border-color: var(--color-teal);
  background: rgba(26, 139, 157, 0.05);
  color: var(--color-teal);
}

.add-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.add-store-card:hover .add-icon {
  background: var(--color-teal);
  color: white;
  transform: rotate(90deg);
}
</style>
