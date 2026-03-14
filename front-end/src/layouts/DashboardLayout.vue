<template>
  <div class="w-full min-h-screen flex flex-col md:flex-row bg-[#080d14]">
    <!-- Header Mobile (Burger) -->
    <header
      class="md:hidden bg-black/80 backdrop-blur-[20px] border-b border-teal/30 shadow-glow-teal sticky top-0 z-[100] px-6 h-[70px] flex justify-between items-center"
    >
      <div class="logo-section flex items-center gap-4">
        <router-link to="/dashboard" class="no-underline flex items-center gap-2">
          <component :is="BarChart2" :size="24" class="text-teal" />
          <h1 class="text-[1.2rem] font-bold text-teal shadow-teal [text-shadow:0_0_10px_var(--color-teal)]">
            Dashboard
          </h1>
        </router-link>
      </div>

      <button
        class="text-teal focus:outline-none transition-transform hover:scale-110"
        @click="toggleMobileMenu"
        aria-label="Toggle Menu"
      >
        <component :is="isMobileMenuOpen ? X : Menu" :size="28" />
      </button>
    </header>

    <!-- Sidebar Desktop / Menu Mobile -->
    <aside
      class="bg-black/90 md:bg-black/40 backdrop-blur-[20px] border-r border-teal/30 shadow-glow-teal fixed md:sticky top-[70px] md:top-0 left-0 w-full md:w-[280px] h-[calc(100vh-70px)] md:h-screen z-[90] flex flex-col transition-transform duration-300 md:translate-x-0"
      :class="isMobileMenuOpen ? 'translate-x-0' : '-translate-x-full'"
    >
      <div class="hidden md:flex items-center justify-center h-[100px] px-8 border-b border-teal/10 shrink-0">
        <router-link
          to="/dashboard"
          class="no-underline w-full flex items-center justify-center gap-2 text-teal shadow-teal [text-shadow:0_0_10px_var(--color-teal)]"
        >
          <component :is="BarChart2" :size="28" />
          <h1 class="text-[1.5rem] font-bold m-0 w-full text-left">Dashboard</h1>
        </router-link>
      </div>

      <nav class="flex-1 overflow-hidden py-6 px-4 flex flex-col gap-4">
        <router-link
          to="/dashboard"
          @click="isMobileMenuOpen = false"
          class="text-white no-underline font-medium text-[1.1rem] transition-all duration-300 cursor-pointer flex items-center p-4 rounded-xl border border-transparent hover:text-teal hover:[text-shadow:0_0_10px_var(--color-teal)] hover:bg-teal/5 hover:border-teal/30 hover:pl-6 router-link-active:text-teal router-link-active:[text-shadow:0_0_10px_var(--color-teal)] router-link-active:bg-teal/10 router-link-active:border-teal/50"
        >
          <component :is="Home" :size="20" class="mr-3 shrink-0" /> Accueil
        </router-link>

        <div
          class="flex flex-col gap-2 mt-4 flex-1 overflow-hidden"
          v-if="activeApplications.length > 0 || archivedApplications.length > 0"
        >
          <button
            v-if="activeApplications.length > 0"
            class="flex items-center justify-between text-white/40 hover:text-teal transition-colors focus:outline-none px-4 mb-2 cursor-pointer w-full"
            @click="isProjectsExpanded = !isProjectsExpanded"
          >
            <span class="text-[0.8rem] uppercase tracking-[2px] font-bold">Mes Projets</span>
            <component
              :is="ChevronDown"
              :size="16"
              class="transition-transform duration-300"
              :class="isProjectsExpanded ? 'rotate-180' : ''"
            />
          </button>

          <div
            class="flex flex-col overflow-hidden transition-all duration-300"
            :class="isProjectsExpanded ? 'flex-1 opacity-100' : 'max-h-0 opacity-0'"
          >
            <div class="px-2 mb-3 shrink-0">
              <input
                type="text"
                v-model="searchQuery"
                placeholder="Rechercher..."
                class="w-full bg-black/40 border border-teal/20 rounded-lg px-3 py-2 text-white/80 text-sm focus:outline-none focus:border-teal/60 transition-colors placeholder:text-white/20"
              />
            </div>

            <div class="flex-1 overflow-y-auto pr-2 custom-scrollbar flex flex-col gap-1 mx-1">
              <div
                v-for="application in filteredGames"
                :key="application.id"
                class="flex items-center gap-3 p-3 rounded-lg text-white/70 cursor-pointer transition-all duration-200 border border-transparent hover:bg-teal/5 hover:text-teal hover:border-teal/20"
                :class="{ 'bg-teal/10 text-teal border-teal/50': $route.path === `/application/${application.id}` }"
                @click="navigateToGame(application.id)"
              >
                <component :is="Gamepad2" :size="20" class="shrink-0 text-white/70" />
                <span class="font-medium whitespace-nowrap overflow-hidden text-ellipsis">{{ application.name }}</span>
              </div>
              <div v-if="filteredGames.length === 0" class="text-white/30 text-sm text-center italic py-4">
                Aucun projet trouvé.
              </div>
            </div>
          </div>

          <div class="mt-4" v-if="archivedApplications.length > 0">
            <button
              class="flex items-center justify-between text-white/20 hover:text-teal transition-colors focus:outline-none px-4 mb-2 cursor-pointer w-full"
              @click="isArchivesExpanded = !isArchivesExpanded"
            >
              <span class="text-[0.8rem] uppercase tracking-[2px] font-bold">Archives</span>
              <component
                :is="ChevronDown"
                :size="16"
                class="transition-transform duration-300"
                :class="isArchivesExpanded ? 'rotate-180' : ''"
              />
            </button>

            <div
              class="flex flex-col overflow-hidden transition-all duration-300"
              :class="isArchivesExpanded ? 'opacity-100 max-h-[300px]' : 'max-h-0 opacity-0'"
            >
              <div class="flex flex-col gap-1 mx-1 px-4">
                <div
                  v-for="app in archivedApplications"
                  :key="app.id"
                  class="flex items-center gap-3 p-2 rounded-lg text-white/30 cursor-pointer transition-all duration-200 hover:bg-white/5 hover:text-white/60"
                  @click="navigateToGame(app.id)"
                >
                  <component :is="Archive" :size="16" class="shrink-0" />
                  <span class="text-sm whitespace-nowrap overflow-hidden text-ellipsis">{{ app.name }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="mt-auto border-t border-teal/20 pt-8 flex flex-col gap-2">
          <router-link
            to="/"
            class="text-white/50 no-underline font-medium text-[0.95rem] transition-all duration-300 cursor-pointer flex items-center p-3 rounded-lg hover:text-white hover:bg-white/5"
          >
            <component :is="Globe" :size="20" class="mr-3 opacity-70" /> Retour Vitrine
          </router-link>

          <a
            href="#"
            @click.prevent="logout"
            class="text-red-400 no-underline font-medium text-[0.95rem] transition-all duration-300 cursor-pointer flex items-center p-3 rounded-lg hover:text-white hover:bg-red-500/10 hover:border-red-500/30 border border-transparent"
          >
            <component :is="LogOut" :size="20" class="mr-3 opacity-70" /> Déconnexion
          </a>
        </div>
      </nav>
    </aside>

    <!-- Main Content -->
    <main
      class="flex-1 min-w-0 w-full max-w-[1400px] mx-auto p-4 md:p-8 mt-4 md:mt-0 min-h-[calc(100vh-70px)] md:min-h-screen"
    >
      <slot />
    </main>

    <ConfirmModal
      :show="showLogoutConfirm"
      title="Déconnexion"
      message="Voulez-vous vraiment vous déconnecter de votre session ?"
      confirm-text="Se déconnecter"
      is-dangerous
      @confirm="executeLogout"
      @cancel="showLogoutConfirm = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth.service'
import { useDashboardStore } from '@/stores/useDashboardStore'
import ConfirmModal from '@/components/Dashboard/ui/ConfirmModal.vue'
import { BarChart2, Menu, X, Home, ChevronDown, Gamepad2, Globe, LogOut, Archive } from 'lucide-vue-next'

const router = useRouter()
const store = useDashboardStore()

const isMobileMenuOpen = ref(false)
const isProjectsExpanded = ref(true)
const isArchivesExpanded = ref(false)
const showLogoutConfirm = ref(false)
const searchQuery = ref('')

const activeApplications = computed(() => {
  return store.applications.filter((app) => app.active !== false)
})

const archivedApplications = computed(() => {
  return store.applications.filter((app) => app.active === false)
})

const filteredGames = computed(() => {
  const apps = activeApplications.value
  if (!searchQuery.value) return apps
  const query = searchQuery.value.toLowerCase()
  return apps.filter((application) => application.name.toLowerCase().includes(query))
})

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

const logout = () => {
  showLogoutConfirm.value = true
}

const executeLogout = () => {
  authService.logout()
  router.push('/login')
}

const navigateToGame = (applicationId: string) => {
  isMobileMenuOpen.value = false
  router.push(`/application/${applicationId}`)
}

onMounted(() => {
  store.fetchApplications()
})
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: color-mix(in srgb, var(--color-teal) 5%, transparent);
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: color-mix(in srgb, var(--color-teal) 30%, transparent);
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: color-mix(in srgb, var(--color-teal) 60%, transparent);
}
</style>
