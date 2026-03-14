import { ref } from 'vue'
import { getErrorMessage } from '@/utils/error'

export type ToastType = 'success' | 'warning' | 'error' | 'info'

export interface Toast {
  id: number
  message: string
  type: ToastType
}

export const toasts = ref<Toast[]>([])
let nextId = 0

const addToast = (message: string, type: ToastType = 'success', duration = 3000) => {
  const id = nextId++
  toasts.value.push({ id, message, type })
  setTimeout(() => {
    removeToast(id)
  }, duration)
}

export const removeToast = (id: number) => {
  toasts.value = toasts.value.filter((t) => t.id !== id)
}

export const toast = {
  success: (message: string, duration?: number) => addToast(message, 'success', duration),
  warning: (message: string, duration?: number) => addToast(message, 'warning', duration),
  info: (message: string, duration?: number) => addToast(message, 'info', duration),
  error: (error: unknown, fallbackMessage?: string, duration?: number) => {
    const msg =
      typeof error === 'string' && !fallbackMessage
        ? error
        : getErrorMessage(error, fallbackMessage || 'Une erreur est survenue')
    addToast(msg, 'error', duration)
  }
}

export function useToast() {
  return { toasts, removeToast, toast }
}
