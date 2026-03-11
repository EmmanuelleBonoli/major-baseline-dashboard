<template>
  <div class="fixed top-4 right-4 z-[9999] flex flex-col gap-2">
    <TransitionGroup name="toast">
      <div
        v-for="toast in toasts"
        :key="toast.id"
        class="px-5 py-4 rounded shadow-lg flex items-center gap-3 text-white border min-w-[300px] font-bold tracking-wide"
        :class="{
          'bg-teal/90 border-teal': toast.type === 'success',
          'bg-orange-500/90 border-orange-500': toast.type === 'warning',
          'bg-red-500/90 border-red-500': toast.type === 'error'
        }"
      >
        <span>
          <template v-if="toast.type === 'success'">✓</template>
          <template v-if="toast.type === 'warning'">⚠</template>
          <template v-if="toast.type === 'error'">✕</template>
        </span>
        <span class="flex-1 font-light">{{ toast.message }}</span>
        <button
          @click="removeToast(toast.id)"
          class="opacity-75 hover:opacity-100 transition-opacity text-xl font-bold"
        >
          &times;
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup lang="ts">
import { useToast } from '@/composables/useToast'
const { toasts, removeToast } = useToast()
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>
