<template>
  <div class="flex flex-col gap-2 relative text-left">
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
        :class="[
          'w-full bg-black/40 border-2 text-white p-4 focus:outline-none focus:shadow-glow-teal transition-all font-light text-base md:text-lg',
          errorMessage ? 'input-error' : 'border-slate-700 focus:border-teal hover:border-teal/50'
        ]"
        v-bind="$attrs"
      />
      <textarea
        v-else
        :id="inputId"
        :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
        :class="[
          'w-full bg-black/40 border-2 text-white p-4 focus:outline-none focus:shadow-glow-teal transition-all font-light text-base md:text-lg resize-y',
          errorMessage ? 'input-error' : 'border-slate-700 focus:border-teal hover:border-teal/50'
        ]"
        v-bind="$attrs"
      ></textarea>

      <slot name="suffix" />
    </div>

    <div class="error-slot h-5 overflow-hidden">
      <Transition name="error-fade">
        <p v-if="errorMessage" class="error-text text-[0.78rem] tracking-[1px] font-medium m-0 leading-5">
          ⚠ {{ errorMessage }}
        </p>
      </Transition>
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
  },
  errorMessage: {
    type: String,
    default: ''
  }
})

defineEmits(['update:modelValue'])

const inputId = computed(() => props.id)
</script>

<style scoped>
.error-slot {
  min-height: 1.25rem;
}

.input-error {
  border-color: #c97c6a;
}
.input-error:hover,
.input-error:focus {
  border-color: #d98a78;
}

.error-text {
  color: #c97c6a;
}

.error-fade-enter-active,
.error-fade-leave-active {
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}
.error-fade-enter-from,
.error-fade-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
.error-fade-enter-to,
.error-fade-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
