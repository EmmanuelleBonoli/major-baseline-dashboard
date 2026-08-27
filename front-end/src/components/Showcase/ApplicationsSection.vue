<template>
  <section class="relative py-20 md:py-32 px-0 sm:px-4 md:px-8 z-[2]" id="projects">
    <h2
      class="section-title text-center text-[1.8rem] sm:text-[3.5rem] md:text-[5rem] mb-4 font-black tracking-[1px] sm:tracking-[3px] md:tracking-[5px] uppercase text-white leading-[1.1]"
    >
      Les Projets
    </h2>

    <ProjectFilter v-model="filter" />

    <div class="max-w-[1600px] mx-auto grid grid-cols-[repeat(auto-fit,minmax(min(100%,280px),1fr))] gap-8 md:gap-12">
      <!-- Mock behavior: if filtering by app, applications array might be empty. For now show applications if all or application is selected -->
      <AppCard v-for="application in paginatedGames" :key="application.id" :application="application" />

      <div v-if="filteredGames.length === 0" class="col-span-full text-center text-white/50 italic py-8">
        Aucun projet disponible dans cette catégorie pour le moment.
      </div>
    </div>

    <Pagination v-model="currentPage" :total-pages="totalPages" />
  </section>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import AppCard from './ui/AppCard.vue'
import ProjectFilter from './ui/ProjectFilter.vue'
import Pagination from './ui/Pagination.vue'
import { applications } from '@/content/applications'
import { games } from '@/content/games'
import type { ShowcaseApp } from '@/types/application'

const PAGE_SIZE = 4

const filter = ref('all')
const currentPage = ref(1)

const sortItems = (items: ShowcaseApp[]) => {
  return [...items].sort((a, b) => {
    if (!a.publishDate && !b.publishDate) return 0
    if (!a.publishDate) return -1
    if (!b.publishDate) return 1
    return new Date(b.publishDate).getTime() - new Date(a.publishDate).getTime()
  })
}

const filteredGames = computed(() => {
  if (filter.value === 'all') return sortItems([...applications, ...games])
  if (filter.value === 'app') return sortItems(applications)
  if (filter.value === 'game') return sortItems(games)
  return []
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredGames.value.length / PAGE_SIZE)))

const paginatedGames = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return filteredGames.value.slice(start, start + PAGE_SIZE)
})

watch(filter, () => {
  currentPage.value = 1
})
</script>

<style scoped>
.section-title {
  text-shadow: 0 0 20px var(--color-teal);
}

.filter-btn:not(.active):hover {
  background-color: color-mix(in srgb, var(--color-teal) 10%, transparent);
}

.filter-btn.active {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}
</style>
