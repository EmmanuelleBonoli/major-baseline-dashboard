<template>
  <div class="app-select-container" :class="{ 'is-open': isOpen }" v-click-outside="close">
    <label v-if="label" class="select-label">{{ label }}</label>
    <div class="select-trigger" @click="toggle">
      <div class="selected-value">
        <slot name="prefix" :selected="selectedOption"></slot>
        <span>{{ selectedOption?.label || placeholder || 'Sélectionner...' }}</span>
      </div>
      <component :is="ChevronDown" :size="18" class="chevron" />
    </div>

    <Transition name="fade-slide">
      <div v-if="isOpen" class="select-dropdown glass-panel">
        <div
          v-for="option in options"
          :key="option.value"
          class="select-option"
          :class="{ 'is-selected': modelValue === option.value }"
          @click="select(option)"
        >
          <slot name="option" :option="option">
            {{ option.label }}
          </slot>
          <span v-if="modelValue === option.value" class="check">✓</span>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ChevronDown } from 'lucide-vue-next'

interface Option {
  label: string
  value: any
  icon?: any
}

const props = defineProps<{
  modelValue: any
  options: Option[]
  label?: string
  placeholder?: string
}>()

const emit = defineEmits(['update:modelValue', 'change'])

const isOpen = ref(false)

const selectedOption = computed(() => {
  return props.options.find((opt) => opt.value === props.modelValue)
})

const toggle = () => (isOpen.value = !isOpen.value)
const close = () => (isOpen.value = false)

const select = (option: Option) => {
  emit('update:modelValue', option.value)
  emit('change', option.value)
  isOpen.value = false
}

const vClickOutside = {
  mounted(el: any, binding: any) {
    el._clickOutside = (event: Event) => {
      if (!(el === event.target || el.contains(event.target))) {
        binding.value(event)
      }
    }
    document.addEventListener('click', el._clickOutside)
  },
  unmounted(el: any) {
    document.removeEventListener('click', el._clickOutside)
  }
}
</script>

<style scoped>
.app-select-container {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: auto;
  min-width: 180px;
}

.select-label {
  font-size: 0.75rem;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.4);
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin-left: 0.25rem;
}

.select-trigger {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 0 1.25rem;
  height: 48px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
}

.select-trigger:hover,
.is-open .select-trigger {
  background: rgba(255, 255, 255, 0.08);
  border-color: var(--color-teal);
  box-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 15%, transparent);
}

.selected-value {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-weight: 600;
  font-size: 0.9rem;
}

.chevron {
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  color: var(--color-teal);
}

.is-open .chevron {
  transform: rotate(180deg);
}

.select-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  width: 100%;
  background: #0f172a;
  backdrop-filter: blur(12px);
  border: 1px solid rgba(26, 139, 157, 0.3);
  border-radius: 12px;
  padding: 0.5rem;
  z-index: 100;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  list-style: none;
  margin: 0;
}

.select-option {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.select-option:hover {
  background: rgba(26, 139, 157, 0.15);
  color: var(--color-teal);
  padding-left: 1.25rem;
}

.select-option.is-selected {
  background: rgba(26, 139, 157, 0.1);
  color: var(--color-teal);
  font-weight: 700;
}

.check {
  font-size: 0.8rem;
  color: var(--color-teal);
}

/* Transitions */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
