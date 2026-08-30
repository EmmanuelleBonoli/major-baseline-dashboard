<template>
  <footer
    id="contact"
    class="relative bg-black/90 py-10 md:py-16 px-4 md:px-8 text-center border-t-2 border-teal z-[2] mt-12 md:mt-20 overflow-hidden"
  >
    <p
      class="text-slate-custom text-[0.8rem] md:text-[1.2rem] tracking-[1px] md:tracking-[2px] uppercase flex flex-col md:block items-center gap-1"
    >
      <span>© 2026 Major Baseline</span>
      <span class="md:before:content-['\00a0-\00a0']">Tous droits réservés</span>
    </p>

    <nav
      class="mt-5 flex flex-wrap items-center justify-center gap-x-5 gap-y-2 text-slate-custom text-[0.7rem] md:text-[0.8rem] tracking-[1px] uppercase"
      aria-label="Liens légaux"
    >
      <RouterLink to="/mentions-legales" class="footer-link">Mentions légales</RouterLink>
      <span class="opacity-40" aria-hidden="true">/</span>
      <RouterLink to="/confidentialite" class="footer-link">Politique de confidentialité</RouterLink>
      <span class="opacity-40" aria-hidden="true">/</span>
      <button type="button" class="footer-link" @click="openBanner">Gérer les cookies</button>
    </nav>
  </footer>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { useConsent } from '@/composables/useConsent'

const { openBanner } = useConsent()
</script>

<style scoped>
.footer-link {
  color: inherit;
  text-decoration: none;
  transition: color 0.2s ease;
  cursor: pointer;
  background: none;
  border: none;
  font: inherit;
  text-transform: inherit;
  letter-spacing: inherit;
}

.footer-link:hover {
  color: var(--color-teal);
}
</style>
