<template>
  <header class="header-container fixed w-full top-0 z-[1000] bg-black/80 backdrop-blur-[20px] border-b border-teal/30">
    <nav class="max-w-[1600px] mx-auto px-4 md:px-12 py-2 md:py-6 flex justify-between items-center">
      <router-link
        :to="{ path: '/', hash: '#home' }"
        class="logo flex items-center h-14 md:h-20 lg:h-22 w-auto transition-all relative z-[1002] cursor-pointer"
        :style="isMenuOpen ? 'filter: grayscale(1) brightness(0.2); opacity: 0.5;' : ''"
        aria-label="Retour à l'accueil"
      >
        <img src="/major-baseline-logo-dark.svg" alt="Major Baseline Logo" class="h-full w-auto object-contain" />
      </router-link>

      <!-- Bouton Burger (Mobile) -->
      <button
        class="md:hidden text-teal text-3xl focus:outline-none z-[1002] transition-transform hover:scale-110"
        @click="toggleMenu"
        aria-label="Toggle Menu"
      >
        <span v-if="!isMenuOpen">☰</span>
        <span v-else>✕</span>
      </button>

      <!-- Menu Desktop -->
      <div class="hidden md:flex flex-1 items-center justify-end gap-x-6 lg:gap-x-12 ml-6 lg:ml-12 min-w-0">
        <ul class="nav-list list-none m-0 p-0">
          <li v-for="(link, index) in links" :key="index">
            <router-link
              :to="link.hash ? { path: link.path, hash: link.hash } : link.path"
              class="nav-item text-white no-underline font-bold text-[0.9rem] lg:text-[1.1rem] tracking-[1.5px] lg:tracking-[2.5px] uppercase relative px-6 lg:px-8 py-2 flex items-center group whitespace-nowrap"
              :class="{ 'active-link': isLinkActive(link) }"
            >
              <span class="indicator absolute left-0 opacity-0 text-gold transition-all duration-300 text-xs">▶</span>
              <span class="label transition-transform duration-300">{{ link.label }}</span>
            </router-link>
          </li>
        </ul>
        <router-link
          :to="isAuthenticated ? '/dashboard' : '/login'"
          class="btn-primary rounded py-2.5 px-6 lg:px-8 no-underline font-bold text-[0.95rem] lg:text-[1.1rem] tracking-[2px] uppercase shrink-0 ml-4 h-fit text-white"
        >
          {{ isAuthenticated ? 'DASHBOARD' : 'CONNEXION' }}
        </router-link>
      </div>

      <!-- Menu Mobile -->
      <div
        class="fixed top-0 left-0 w-screen h-[100dvh] bg-black/95 backdrop-blur-md z-[1001] flex flex-col items-center justify-center transition-all duration-500 md:hidden"
        :class="isMenuOpen ? 'opacity-100 visible' : 'opacity-0 invisible pointer-events-none'"
      >
        <ul class="flex flex-col gap-10 list-none items-center text-center">
          <li v-for="(link, index) in links" :key="index" @click="toggleMenu">
            <router-link
              :to="link.hash ? { path: link.path, hash: link.hash } : link.path"
              class="nav-item-mobile text-white no-underline font-bold text-[1.5rem] tracking-[3px] uppercase"
            >
              {{ link.label }}
            </router-link>
          </li>
          <li class="mt-8" @click="toggleMenu">
            <router-link
              :to="isAuthenticated ? '/dashboard' : '/login'"
              class="btn-primary text-white border-2 border-teal rounded py-3 px-8 no-underline font-black text-[1.5rem] tracking-[3px] uppercase inline-block"
            >
              {{ isAuthenticated ? 'DASHBOARD' : 'CONNEXION' }}
            </router-link>
          </li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isMenuOpen = ref(false)
const isAuthenticated = ref(false)
const activeSection = ref('home')

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const isLinkActive = (link: any) => {
  // Si pas sur la page d'accueil, on vérifie juste le chemin
  if (route.path !== '/') {
    return route.path === link.path
  }

  // Si sur la page d'accueil, on vérifie le chemin ET la section active (calculée par le scroll)
  if (link.path === '/') {
    const linkHash = link.hash?.replace('#', '') || 'home'
    return activeSection.value === linkHash
  }

  return false
}

onMounted(() => {
  isAuthenticated.value = !!localStorage.getItem('majorBaselineToken')

  const observerOptions = {
    root: null,
    rootMargin: '-40% 0px -40% 0px', // Détecte la section quand elle est au milieu de l'écran
    threshold: 0
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        activeSection.value = entry.target.id
      }
    })
  }, observerOptions)

  // On observe toutes les sections principales
  const sections = ['home', 'projects', 'studio']
  sections.forEach((id) => {
    const el = document.getElementById(id)
    if (el) observer.observe(el)
  })
})

const links = [
  { path: '/', hash: '#home', label: 'Accueil' },
  { path: '/', hash: '#projects', label: 'Projets' },
  { path: '/', hash: '#studio', label: 'Studio' },
  { path: '/contact', hash: null, label: 'Contact' }
]
</script>

<style scoped>
.header-container {
  box-shadow: 0 0 30px color-mix(in srgb, var(--color-teal) 20%, transparent);
}

.logo {
  transition: opacity 0.3s ease;
}
.logo:hover {
  opacity: 0.85;
}

.nav-list {
  display: grid;
  grid-template-columns: repeat(2, auto);
  justify-items: end;
  column-gap: 0.5rem;
  row-gap: 0.25rem;
}

@media (min-width: 1200px) {
  .nav-list {
    display: flex;
    flex-wrap: nowrap;
    grid-template-columns: none;
    gap: 0;
  }
}

.nav-item {
  transition: all 0.3s ease;
}

.nav-item:hover,
.nav-item.active-link {
  color: var(--color-teal);
  text-shadow: 0 0 10px var(--color-teal);
}

.nav-item:hover .indicator,
.nav-item.active-link .indicator {
  opacity: 1;
  transform: translateX(8px);
}

.nav-item:hover .label,
.nav-item.active-link .label {
  transform: translateX(12px);
}

.nav-item-mobile {
  transition: all 0.3s ease;
}
.nav-item-mobile:hover {
  color: var(--color-teal);
}

.btn-primary {
  transition: all 0.3s ease;
}
.btn-primary:hover {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 20px var(--color-teal);
  transform: translateY(-4px);
}
</style>
