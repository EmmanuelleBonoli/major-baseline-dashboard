<template>
  <button 
    :class="[
      'inline-flex items-center justify-center transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed cursor-pointer',
      variant === 'primary' ? 'bg-teal text-black font-black uppercase tracking-[2px] hover:bg-gold hover:shadow-glow-gold-lg hover:-translate-y-1' : '',
      variant === 'outline' ? 'bg-transparent text-teal border-2 border-teal font-bold tracking-[2px] uppercase hover:bg-teal hover:text-black hover:shadow-glow-teal hover:-translate-y-1' : '',
      variant === 'ghost' ? 'bg-transparent text-white/60 hover:text-teal hover:shadow-glow-teal font-medium uppercase tracking-[1px] hover:bg-black/80 border border-transparent hover:border-teal/50' : ''
    ]"
    :disabled="disabled || loading"
    v-bind="$attrs"
  >
    <span v-if="loading" class="mr-2 opacity-70">⟳</span>
    <slot />
  </button>
</template>

<script setup lang="ts">
defineProps({
  variant: {
    type: String,
    default: 'primary' // primary, outline, ghost
  },
  loading: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  }
})
</script>
