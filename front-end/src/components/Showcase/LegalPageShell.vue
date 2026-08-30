<template>
  <div class="legal-page min-h-screen flex flex-col bg-[color:var(--color-deep-blue)] text-white">
    <HeaderSection />

    <!-- Compense la hauteur du header fixe. -->
    <div class="w-full shrink-0 h-20 md:h-28 lg:h-32"></div>

    <main class="flex-1 w-full max-w-[860px] mx-auto px-4 sm:px-6 py-8 md:py-14">
      <h1
        class="text-[1.8rem] sm:text-[2.4rem] md:text-[3rem] font-black uppercase tracking-[2px] md:tracking-[3px] leading-tight text-white"
      >
        {{ title }}
      </h1>
      <p v-if="updatedAt" class="text-gold uppercase tracking-[2px] text-[0.75rem] md:text-[0.85rem] mt-3 font-bold">
        Dernière mise à jour : {{ updatedAt }}
      </p>

      <div class="legal-prose mt-8 md:mt-12">
        <slot />
      </div>
    </main>

    <FooterSection />
  </div>
</template>

<script setup lang="ts">
import HeaderSection from '@/components/Showcase/HeaderSection.vue'
import FooterSection from '@/components/Showcase/FooterSection.vue'

defineProps<{
  title: string
  updatedAt?: string
}>()
</script>

<style>
.legal-prose {
  color: color-mix(in srgb, #ffffff 82%, transparent);
  line-height: 1.7;
  font-size: 0.95rem;
}

.legal-prose h2 {
  color: #ffffff;
  font-family: var(--font-display);
  font-size: 1.15rem;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  margin: 2.5rem 0 0.9rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid color-mix(in srgb, var(--color-teal) 30%, transparent);
}

.legal-prose h3 {
  color: color-mix(in srgb, #ffffff 92%, transparent);
  font-weight: 700;
  font-size: 1rem;
  margin: 1.6rem 0 0.6rem;
}

.legal-prose p {
  margin: 0.8rem 0;
}

.legal-prose ul {
  margin: 0.8rem 0;
  padding-left: 1.4rem;
  list-style: square;
}

.legal-prose li {
  margin: 0.35rem 0;
}

.legal-prose a {
  color: var(--color-gold);
  text-decoration: underline;
  text-underline-offset: 3px;
}

.legal-prose a:hover {
  color: var(--color-gold-soft);
}

.legal-prose strong {
  color: #ffffff;
}

.legal-prose code {
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 0.85em;
  background: color-mix(in srgb, var(--color-teal) 15%, transparent);
  padding: 0.1em 0.4em;
  border-radius: 3px;
}

.legal-prose table {
  width: 100%;
  border-collapse: collapse;
  margin: 1.2rem 0;
  font-size: 0.85rem;
}

.legal-prose th,
.legal-prose td {
  border: 1px solid color-mix(in srgb, var(--color-teal) 25%, transparent);
  padding: 0.55rem 0.7rem;
  text-align: left;
  vertical-align: top;
}

.legal-prose th {
  background: color-mix(in srgb, var(--color-teal) 12%, transparent);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 0.78rem;
}

.legal-prose .table-scroll {
  overflow-x: auto;
}
</style>
