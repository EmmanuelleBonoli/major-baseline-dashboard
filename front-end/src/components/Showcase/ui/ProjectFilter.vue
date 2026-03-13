<template>
  <div class="flex justify-center mb-12">
    <!-- Filtres Desktop -->
    <div class="hidden sm:flex gap-4 flex-wrap justify-center">
      <button
        v-for="(label, key) in filterLabels"
        :key="key"
        @click="$emit('update:modelValue', key)"
        :class="[
          'filter-btn px-6 py-2 border border-teal rounded-full text-sm font-bold tracking-[1px] uppercase transition-all cursor-pointer',
          modelValue === key ? 'active' : 'text-teal'
        ]"
      >
        {{ label }}
      </button>
    </div>

    <!-- Filtres Mobile -->
    <div class="sm:hidden w-full max-w-[300px] px-2">
      <div class="relative custom-dropdown">
        <button
          @click="isDropdownOpen = !isDropdownOpen"
          class="w-full bg-black/80 border-2 border-teal text-teal font-black uppercase tracking-[2px] py-4 px-4 flex justify-between items-center transition-all"
          :class="{ 'border-gold text-gold': isDropdownOpen }"
        >
          <span class="truncate mr-2">Filtre : {{ filterLabels[modelValue] }}</span>
          <span class="transition-transform duration-300 shrink-0" :class="{ 'rotate-180': isDropdownOpen }"> ▼ </span>
        </button>
        <div
          v-if="isDropdownOpen"
          class="absolute top-full left-0 w-full bg-black border-2 border-teal border-t-0 z-50 shadow-[0_10px_30px_rgba(0,0,0,0.8)]"
        >
          <button
            v-for="(label, key) in filterLabels"
            :key="key"
            @click="setFilter(key as string)"
            class="w-full text-left py-4 px-6 text-teal font-bold uppercase tracking-[1px] hover:bg-teal/10 hover:text-white border-b border-teal/10 last:border-0 transition-colors"
            :class="{ 'bg-teal/20 text-white': modelValue === key }"
          >
            {{ label }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps<{
  modelValue: string
}>()

const emit = defineEmits(['update:modelValue'])

const isDropdownOpen = ref(false)

const filterLabels: Record<string, string> = {
  all: 'Tous',
  app: 'Applications',
  game: 'Jeux'
}

const setFilter = (val: string) => {
  emit('update:modelValue', val)
  isDropdownOpen.value = false
}
</script>

<style scoped>
.filter-btn:not(.active):hover {
  background-color: color-mix(in srgb, var(--color-teal) 10%, transparent);
}

.filter-btn.active {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.custom-dropdown button {
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
}

.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
