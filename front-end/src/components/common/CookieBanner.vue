<template>
  <Transition name="cookie">
    <section
      v-if="isMounted && isBannerOpen"
      ref="bannerRef"
      class="fixed inset-x-0 bottom-0 z-[9998] p-4 md:right-6 md:bottom-6 md:inset-x-auto md:max-w-[420px]"
      role="dialog"
      aria-modal="false"
      aria-labelledby="cookie-banner-title"
      aria-describedby="cookie-banner-desc"
      tabindex="-1"
    >
      <div class="bg-black/95 border border-teal/40 backdrop-blur-md p-5 md:p-6 shadow-glow-teal">
        <h2
          id="cookie-banner-title"
          class="text-white font-black uppercase tracking-[2px] text-[0.95rem] md:text-[1.05rem]"
        >
          Cookies & mesure d'audience
        </h2>

        <p id="cookie-banner-desc" class="text-slate-custom text-[0.85rem] leading-relaxed mt-3">
          Ce site utilise Google Analytics pour mesurer sa fréquentation. Ces cookies ne sont déposés qu'avec votre
          accord et vous pouvez modifier votre choix à tout moment.
          <RouterLink to="/confidentialite" class="text-gold underline underline-offset-2 hover:text-gold-soft">
            En savoir plus
          </RouterLink>
        </p>

        <div class="flex flex-col sm:flex-row gap-3 mt-5">
          <button
            type="button"
            class="flex-1 px-5 py-3 bg-teal text-black font-black uppercase tracking-[1.5px] text-[0.8rem] transition-all duration-300 hover:bg-gold hover:shadow-glow-gold-lg hover:-translate-y-0.5 cursor-pointer"
            @click="accept"
          >
            Tout accepter
          </button>
          <button
            type="button"
            class="flex-1 px-5 py-3 bg-transparent text-teal border-2 border-teal font-bold uppercase tracking-[1.5px] text-[0.8rem] transition-all duration-300 hover:bg-teal hover:text-black hover:-translate-y-0.5 cursor-pointer"
            @click="refuse"
          >
            Tout refuser
          </button>
        </div>
      </div>
    </section>
  </Transition>
</template>

<script setup lang="ts">
import { onMounted, ref, nextTick } from 'vue'
import { RouterLink } from 'vue-router'
import { useConsent } from '@/composables/useConsent'

const { isBannerOpen, accept, refuse } = useConsent()

const isMounted = ref(false)
const bannerRef = ref<HTMLElement | null>(null)

onMounted(async () => {
  isMounted.value = true
  // Place le focus sur le bandeau pour les utilisateurs au clavier / lecteur d'écran.
  if (isBannerOpen.value) {
    await nextTick()
    bannerRef.value?.focus()
  }
})
</script>

<style scoped>
.cookie-enter-active,
.cookie-leave-active {
  transition:
    opacity 0.3s ease,
    transform 0.3s ease;
}
.cookie-enter-from,
.cookie-leave-to {
  opacity: 0;
  transform: translateY(1rem);
}
</style>
