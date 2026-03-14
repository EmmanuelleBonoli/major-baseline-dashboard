<template>
  <Modal :show="show" :title="title" width="400px" @close="$emit('cancel')">
    <div class="confirm-content">
      <p>{{ message }}</p>
    </div>
    <template #footer>
      <button class="btn-ghost" @click="$emit('cancel')">{{ cancelText || 'Annuler' }}</button>
      <button
        class="border-none py-2 px-4 rounded-xl font-bold cursor-pointer transition-all"
        :class="isDangerous ? 'bg-red-500 hover:bg-red-600 text-white' : 'bg-teal hover:bg-teal-600 text-white'"
        @click="$emit('confirm')"
      >
        {{ confirmText || 'Confirmer' }}
      </button>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from './Modal.vue'

defineProps<{
  show: boolean
  title: string
  message: string
  confirmText?: string
  cancelText?: string
  isDangerous?: boolean
}>()

defineEmits(['confirm', 'cancel'])
</script>

<style scoped>
.confirm-content p {
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
  margin: 0;
}
</style>
