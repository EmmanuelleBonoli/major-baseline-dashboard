<template>
  <div class="w-full">
    <div class="fixed top-0 left-0 w-full h-full z-[-1] bg-gradient-background"></div>
    <div class="fixed top-0 left-0 w-full h-full z-[-1] bg-grid-pattern"></div>

    <HeaderSection />

    <HeroSection />
    <ApplicationsSection />
    <StudioSection />

    <FooterSection />
  </div>
</template>

<script setup lang="ts">
import { useParticles } from '../composables/useParticles.ts'
import HeaderSection from '../components/Showcase/HeaderSection.vue'
import HeroSection from '../components/Showcase/HeroSection.vue'
import ApplicationsSection from '../components/Showcase/ApplicationsSection.vue'
import StudioSection from '../components/Showcase/StudioSection.vue'
import FooterSection from '../components/Showcase/FooterSection.vue'

useParticles()
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

@keyframes gridScroll {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(50px);
  }
}
</style>
