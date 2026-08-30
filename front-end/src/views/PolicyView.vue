<template>
  <div class="policy-view min-h-screen bg-[#FAF8F5] text-[#333]">
    <div v-if="loading" class="flex items-center justify-center min-h-screen">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-teal-600"></div>
    </div>
    <div v-else-if="error" class="flex flex-col items-center justify-center min-h-screen p-4 text-center">
      <h1 class="text-4xl font-bold text-red-600 mb-4">404</h1>
      <p class="text-xl text-gray-700 mb-8">Document juridique introuvable.</p>
      <router-link to="/" class="text-teal-600 hover:underline">Retour à l'accueil</router-link>
    </div>
    <div v-else v-html="policyContent"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useSEO } from '@/composables/useSEO'

const route = useRoute()
const policyContent = ref('')
const loading = ref(true)
const error = ref(false)

// Titre dérivé des paramètres d'URL : plus explicite qu'un générique
// « Document Légal » pour l'indexation et l'affichage en onglet.
const seoTitle = computed(() => {
  const project = String(route.params.projectId ?? '').trim()
  const label = route.params.type === 'privacy' ? 'Politique de confidentialité' : 'Conditions générales de vente'
  return project ? `${label} — ${project}` : 'Document légal'
})

useSEO({ title: seoTitle })

const policies = import.meta.glob('../content/policies/*.html', { query: '?raw', import: 'default' })

onMounted(async () => {
  const { projectId, type } = route.params

  const policyType = type === 'privacy' ? 'privacy_policy' : 'sales_policy'
  const fileName = `../content/policies/${projectId}_${policyType}.html`

  try {
    if (policies[fileName]) {
      const content = await (policies[fileName] as () => Promise<string>)()
      policyContent.value = content
    } else {
      error.value = true
    }
  } catch (e) {
    console.error('Error loading policy:', e)
    error.value = true
  } finally {
    loading.value = false
  }
})
</script>

<style>
/* Reset des styles pour le document HTML injecté */
.policy-view {
  font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}
</style>
