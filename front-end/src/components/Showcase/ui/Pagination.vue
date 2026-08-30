<template>
  <div v-if="totalPages > 1" class="flex justify-center items-center gap-2 md:gap-3 mt-12 md:mt-16 flex-wrap">
    <button
      @click="go(modelValue - 1)"
      :disabled="modelValue === 1"
      class="pagination-btn w-10 h-10 md:w-12 md:h-12 flex items-center justify-center border border-teal rounded-full text-teal font-bold transition-all disabled:opacity-30 disabled:cursor-not-allowed"
      aria-label="Page précédente"
    >
      ‹
    </button>

    <button
      v-for="page in totalPages"
      :key="page"
      @click="go(page)"
      :class="[
        'pagination-btn w-10 h-10 md:w-12 md:h-12 flex items-center justify-center border border-teal rounded-full font-bold tracking-[1px] transition-all',
        modelValue === page ? 'active' : 'text-teal'
      ]"
      :aria-label="`Page ${page}`"
      :aria-current="modelValue === page ? 'page' : undefined"
    >
      {{ page }}
    </button>

    <button
      @click="go(modelValue + 1)"
      :disabled="modelValue === totalPages"
      class="pagination-btn w-10 h-10 md:w-12 md:h-12 flex items-center justify-center border border-teal rounded-full text-teal font-bold transition-all disabled:opacity-30 disabled:cursor-not-allowed"
      aria-label="Page suivante"
    >
      ›
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  modelValue: number
  totalPages: number
}>()

const emit = defineEmits<{
  'update:modelValue': [page: number]
}>()

const go = (page: number) => {
  emit('update:modelValue', page)
}
</script>

<style scoped>
.pagination-btn {
  cursor: pointer;
}

.pagination-btn:not(.active):not(:disabled):hover {
  background-color: color-mix(in srgb, var(--color-teal) 10%, transparent);
}

.pagination-btn.active {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}
</style>
