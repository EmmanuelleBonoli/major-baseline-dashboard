<template>
  <div class="w-full text-white min-h-screen flex flex-col">

    <div class="bg-gradient-background fixed top-0 left-0 w-full h-full z-[-1]"></div>
    <div class="bg-grid-pattern fixed top-0 left-0 w-full h-full z-[-1]"></div>

    <div
      class="px-4 py-4 md:p-8 flex justify-between items-center max-w-[1600px] mx-auto absolute top-0 w-full left-1/2 -translate-x-1/2 z-10"
    >
      <router-link
        to="/"
        class="btn-back flex items-center gap-2 text-teal uppercase tracking-[1px] md:tracking-[3px] text-sm md:text-base font-bold py-2 md:py-2 px-4 md:px-6 border-2 border-teal transition-all duration-300"
      >
        &lt; Retour au site
      </router-link>
    </div>

    <div
      v-if="application"
      class="max-w-[1200px] mx-auto px-4 sm:px-8 pt-20 pb-16 md:py-32 relative z-[2] flex flex-col items-center"
    >
      <div
        class="banner-container relative w-full h-[200px] md:h-[400px] border-[1px] md:border-2 border-teal/50 overflow-hidden mb-8 md:mb-16 flex items-center justify-center"
      >
        <div class="banner-icon text-[6rem] md:text-[12rem] z-10">
          {{ application.icon }}
        </div>
      </div>

      <div
        class="w-full mb-8 md:mb-12 flex flex-col md:flex-row justify-between items-start md:items-end gap-4 md:gap-6 border-b border-teal/30 pb-8 md:pb-12"
      >
        <div class="w-full">
          <h1
            class="title-glow text-[2rem] sm:text-[3rem] md:text-[4.5rem] font-black tracking-[2px] md:tracking-[4px] uppercase text-white mb-2 md:mb-4 leading-tight"
          >
            {{ application.title }}
          </h1>
          <p class="text-[1.1rem] md:text-[1.5rem] text-gold tracking-[1px] md:tracking-[2px] font-light leading-snug">
            {{ application.tagline }}
          </p>
        </div>
        <div class="flex flex-col items-start md:items-end w-full md:w-auto mt-4 md:mt-0">
          <div
            class="status-badge inline-flex items-center gap-3 text-gold font-black uppercase tracking-[2px] md:tracking-[3px] bg-black/50 px-4 py-2 md:px-6 md:py-3 border border-gold/50 text-[0.85rem] md:text-[1rem] whitespace-nowrap"
          >
            <span class="w-2.5 h-2.5 md:w-3 md:h-3 bg-gold rounded-full pulse-glow"></span>
            STATUT: {{ application.status }}
          </div>
          <div
            v-if="application.publishDate"
            class="inline-flex items-center text-teal text-[0.7rem] md:text-[0.85rem] font-bold tracking-[2px] uppercase mt-3 px-2"
          >
            DATE DE PUBLICATION :
            {{
              new Date(application.publishDate).toLocaleDateString('fr-FR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
              })
            }}
          </div>
        </div>
      </div>

      <div class="w-full flex gap-3 md:gap-4 mb-8 md:mb-12 flex-wrap">
        <span
          v-for="category in application.category"
          :key="category"
          class="category-tag py-1 px-4 md:py-2 md:px-6 bg-teal/10 border border-teal text-teal uppercase tracking-[1px] md:tracking-[2px] font-bold text-[0.75rem] md:text-[1rem] backdrop-blur-sm"
        >
          {{ category }}
        </span>
      </div>

      <div
        class="description-card w-full p-6 md:p-10 bg-black/60 border border-slate-700/50 backdrop-blur-md mb-10 md:mb-16 relative"
      >
        <h2
          class="text-xl md:text-2xl font-bold tracking-[2px] md:tracking-[3px] text-teal uppercase mb-4 md:mb-6 flex items-center gap-4"
        >
          <span class="w-6 md:w-8 h-[2px] bg-teal"></span> Détails du Projet
          <span class="w-full h-[1px] bg-gradient-to-r from-teal/20 to-transparent flex-1"></span>
        </h2>
        <div
          class="text-slate-custom text-[0.95rem] md:text-[1.1rem] leading-[1.8] md:leading-[2] font-light whitespace-pre-line"
        >
          {{ application.description || 'Description en cours de rédaction...' }}
        </div>
      </div>
    </div>

    <div v-else class="min-h-[80vh] flex flex-col items-center justify-center relative z-[2] px-4 text-center">
      <div class="text-[8rem] md:text-[10rem] text-teal/20 font-black leading-none">404</div>
      <p class="text-[1.2rem] md:text-[2rem] text-gold uppercase tracking-widest mt-0 md:mt-[-1rem]">
        PROJET INTROUVABLE
      </p>
      <router-link
        to="/"
        class="mt-8 text-teal uppercase tracking-widest text-[0.9rem] md:text-base hover:text-white transition-colors border-b border-teal/50 pb-1"
        >Retour au site</router-link
      >
    </div>

    <FooterSection />
  </div>
</template>

<script setup lang="ts">
import { computed, watchEffect } from 'vue'
import { useRoute } from 'vue-router'
import { gamesMap } from '@/content/games/index'
import { applicationsMap } from '@/content/applications/index'
import { useParticles } from '@/composables/useParticles'
import FooterSection from '@/components/Showcase/FooterSection.vue'

useParticles()

const route = useRoute()

const application = computed(() => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  return gamesMap[id] || applicationsMap[id]
})

watchEffect(() => {
  window.scrollTo(0, 0)
})
</script>

<style scoped>
.bg-gradient-background {
  background-image:
    radial-gradient(ellipse at 20% 30%, color-mix(in srgb, var(--color-teal) 15%, transparent) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 70%, color-mix(in srgb, var(--color-gold) 15%, transparent) 0%, transparent 50%),
    linear-gradient(180deg, var(--color-black) 0%, var(--color-deep-blue) 50%, var(--color-black) 100%);
}

.bg-grid-pattern {
  background-image:
    linear-gradient(color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px),
    linear-gradient(90deg, color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridScroll 20s linear infinite;
}

.btn-back {
  background-color: color-mix(in srgb, var(--color-deep-blue) 60%, transparent);
  clip-path: polygon(0 0, 85% 0, 100% 30%, 100% 100%, 15% 100%, 0 70%);
}

.btn-back:hover {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 20px var(--color-teal);
}

.banner-container {
  background-image: linear-gradient(
    135deg,
    var(--color-deep-blue),
    color-mix(in srgb, var(--color-teal) 20%, var(--color-deep-blue))
  );
  clip-path: polygon(0 0, 95% 0, 100% 10%, 100% 100%, 5% 100%, 0 90%);
}
@media (min-width: 768px) {
  .banner-container {
    clip-path: polygon(0 0, 97% 0, 100% 10%, 100% 100%, 3% 100%, 0 90%);
  }
}
.banner-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background-image: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 2px,
    color-mix(in srgb, var(--color-teal) 10%, transparent) 2px,
    color-mix(in srgb, var(--color-teal) 10%, transparent) 4px
  );
  animation: scanlines 10s linear infinite;
}

.banner-icon {
  animation: float 4s ease-in-out infinite;
  filter: drop-shadow(0 0 20px color-mix(in srgb, var(--color-gold) 80%, transparent));
}
@media (min-width: 768px) {
  .banner-icon {
    filter: drop-shadow(0 0 40px color-mix(in srgb, var(--color-gold) 80%, transparent));
  }
}

.title-glow {
  text-shadow: var(--shadow-glow-teal);
}

.status-badge {
  box-shadow: 0 0 15px color-mix(in srgb, var(--color-gold) 20%, transparent);
}

.pulse-glow {
  animation: pulseLight 2s infinite;
}

.category-tag {
  clip-path: polygon(10px 0, 100% 0, 100% calc(100% - 10px), calc(100% - 10px) 100%, 0 100%, 0 10px);
}

.description-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-image: linear-gradient(to bottom, var(--color-teal), transparent);
}

@keyframes gridScroll {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(50px);
  }
}

@keyframes scanlines {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(50px);
  }
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes pulseLight {
  0%,
  100% {
    opacity: 1;
    box-shadow: 0 0 10px var(--color-gold);
  }
  50% {
    opacity: 0.5;
    box-shadow: 0 0 20px var(--color-gold);
  }
}
</style>
