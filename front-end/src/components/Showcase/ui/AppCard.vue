<template>
  <article class="h-full">
    <router-link
      :to="`/project/${application.id}`"
      class="app-card group cursor-pointer relative bg-black/60 border-y sm:border-2 border-teal overflow-hidden transition-all duration-500 flex flex-col h-full"
      role="link"
      :aria-label="`Voir les détails du projet ${application.title}`"
    >
      <div
        class="card-header w-full h-[200px] md:h-[300px] flex items-center justify-center text-[4rem] md:text-[6rem] relative overflow-hidden shrink-0"
      >
        <span class="card-icon relative z-[2]">{{ application.icon }}</span>
      </div>

      <div class="p-6 sm:p-8 md:p-10 bg-black/80 flex-1 flex flex-col">
        <h3
          class="text-[1.5rem] md:text-[2rem] mb-3 md:mb-4 text-gold uppercase tracking-[2px] md:tracking-[3px] font-black leading-tight"
        >
          {{ application.title }}
        </h3>
        <div class="flex flex-wrap gap-2 mb-4">
          <span
            v-for="category in application.category"
            :key="category"
            class="inline-block py-1 px-2 md:py-2 md:px-4 bg-teal/20 border border-teal text-teal text-[0.7rem] md:text-[0.9rem] uppercase tracking-[1px] font-bold"
          >
            {{ category }}
          </span>
        </div>
        <p class="text-slate-custom text-[0.9rem] md:text-[1rem] leading-[1.6] md:leading-[1.8] mb-6 flex-1">
          {{ application.tagline }}
        </p>

        <div
          class="mt-auto inline-flex items-center gap-2 text-gold font-bold uppercase tracking-[1px] md:tracking-[2px] text-[0.8rem] md:text-[1rem]"
        >
          <span class="pulse-indicator w-2 md:w-2.5 h-2 md:h-2.5 bg-gold rounded-full"></span>
          {{ application.status }}
        </div>
      </div>
    </router-link>
  </article>
</template>

<script setup lang="ts">
import type { ShowcaseApp } from '@/types/application'

defineProps<{
  application: ShowcaseApp
}>()
</script>

<style scoped>
.app-card {
  clip-path: polygon(0 0, 95% 0, 100% 5%, 100% 100%, 5% 100%, 0 95%);
}
.app-card:hover {
  transform: translateY(-16px);
  box-shadow: 0 30px 60px color-mix(in srgb, var(--color-teal) 40%, transparent);
}
.app-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: conic-gradient(var(--color-teal), var(--color-gold), var(--color-teal));
  z-index: -1;
  opacity: 0;
  transition: opacity 0.5s;
  animation: borderRotate 4s linear infinite;
}
.app-card:hover::before {
  opacity: 1;
}

.card-header {
  background: linear-gradient(
    135deg,
    var(--color-deep-blue),
    color-mix(in srgb, var(--color-teal) 30%, var(--color-deep-blue))
  );
}
.card-header::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 2px,
    color-mix(in srgb, var(--color-teal) 10%, transparent) 2px,
    color-mix(in srgb, var(--color-teal) 10%, transparent) 4px
  );
  animation: scanlines 10s linear infinite;
}

.card-icon {
  filter: drop-shadow(0 0 20px var(--color-teal));
  animation: float 3s ease-in-out infinite;
}
.app-card:hover .card-icon {
  filter: drop-shadow(0 0 30px var(--color-gold));
}

.pulse-indicator {
  animation: pulse 2s infinite;
}

@keyframes borderRotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
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

@keyframes pulse {
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
