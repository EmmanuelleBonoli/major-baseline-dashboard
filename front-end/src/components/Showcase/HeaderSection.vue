<template>
  <header class="header-container fixed w-full top-0 z-[1000] bg-black/80 backdrop-blur-[20px] border-b border-teal/30">
    <nav class="max-w-[1600px] mx-auto px-6 md:px-12 py-4 md:py-6 flex justify-between items-center">
      <a href="#home" class="logo flex items-center h-12 md:h-16 lg:h-20 w-auto transition-all relative z-[1002] cursor-pointer" aria-label="Retour en haut">
        <img src="/major-baseline-logo-dark.svg" alt="Major Baseline Logo" class="h-full w-auto object-contain" />
      </a>

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
      <ul class="hidden md:flex gap-8 lg:gap-12 list-none items-center">
        <li v-for="(link, index) in links" :key="index">
          <router-link
              :to="link.hash ? { path: link.path, hash: link.hash } : link.path"
              class="nav-item text-white no-underline font-bold text-[1rem] lg:text-[1.1rem] tracking-[2px] uppercase relative px-2 lg:px-4 py-2 flex items-center group"
          >
            <span class="absolute left-[-15px] opacity-0 text-gold transition-all duration-300 group-hover:opacity-100 group-hover:translate-x-2">▶</span>
            <span class="transition-transform duration-300 group-hover:translate-x-4">{{ link.label }}</span>
          </router-link>
        </li>
        <li>
          <router-link :to="isAuthenticated ? '/dashboard' : '/login'" class="btn-primary text-teal border-2 border-teal rounded py-2 px-6 no-underline font-bold text-[1rem] lg:text-[1.1rem] tracking-[2px] uppercase ml-4 inline-block">
            {{ isAuthenticated ? 'DASHBOARD' : 'CONNEXION' }}
          </router-link>
        </li>
      </ul>

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
            <router-link :to="isAuthenticated ? '/dashboard' : '/login'" class="btn-primary text-teal border-2 border-teal rounded py-3 px-8 no-underline font-black text-[1.5rem] tracking-[3px] uppercase inline-block">
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

const isMenuOpen = ref(false)
const isAuthenticated = ref(false)

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

onMounted(() => {
  isAuthenticated.value = !!localStorage.getItem('majorBaselineToken')
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
  animation: logoGlow 2s ease-in-out infinite;
}

.nav-item {
  transition: all 0.3s ease;
}
.nav-item:hover {
  color: var(--color-teal);
  text-shadow: 0 0 10px var(--color-teal);
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

@keyframes logoGlow {
  0%, 100% { filter: drop-shadow(0 0 2px var(--color-teal)) drop-shadow(0 0 5px var(--color-teal)); }
  50% { filter: drop-shadow(0 0 5px var(--color-teal)) drop-shadow(0 0 10px var(--color-teal)) drop-shadow(0 0 15px var(--color-gold)); }
}
</style>
