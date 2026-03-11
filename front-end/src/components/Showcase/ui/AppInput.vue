<template>
  <div class="flex flex-col gap-3 relative text-left">
    <label
      v-if="label"
      :for="inputId"
      class="text-teal text-[0.8rem] md:text-[0.9rem] uppercase tracking-[2px] font-bold flex items-center gap-2"
    >
      <span v-if="animatedDot" class="w-2 h-2 bg-teal rounded-full animate-pulse" :style="{ animationDelay }"></span>
      {{ label }}
    </label>

    <div class="relative w-full">
      <input
        v-if="!textarea"
        :id="inputId"
        :type="type"
        :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        class="w-full bg-black/40 border-2 border-slate-700 text-white p-4 focus:outline-none focus:border-teal hover:border-teal/50 focus:shadow-glow-teal transition-all font-light text-base md:text-lg"
        v-bind="$attrs"
      />
      <textarea
        v-else
        :id="inputId"
        :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
        class="w-full bg-black/40 border-2 border-slate-700 text-white p-4 focus:outline-none focus:border-teal hover:border-teal/50 focus:shadow-glow-teal transition-all font-light text-base md:text-lg resize-y"
        v-bind="$attrs"
      ></textarea>

      <slot name="suffix" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: ''
  },
  label: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  id: {
    type: String,
    default: () => 'input-' + Math.random().toString(36).substring(2, 9)
  },
  textarea: {
    type: Boolean,
    default: false
  },
  animatedDot: {
    type: Boolean,
    default: false
  },
  animationDelay: {
    type: String,
    default: '0s'
  }
})

defineEmits(['update:modelValue'])

const inputId = computed(() => props.id)
</script>
