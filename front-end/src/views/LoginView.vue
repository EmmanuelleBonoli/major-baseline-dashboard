<template>
  <div class="min-h-[calc(100vh-4rem)] flex items-center justify-center relative">
    <router-link to="/" class="btn-return absolute top-4 left-4 md:top-8 md:left-8 flex items-center gap-2 no-underline font-medium tracking-[1px] uppercase text-[0.9rem] bg-black/40 px-4 py-2 rounded-lg border border-white/10 text-white/60">
      <span class="text-xl leading-none -mt-1">←</span> Retour au site
    </router-link>
    <div class="login-card bg-teal/5 backdrop-blur-md border border-teal/20 rounded-2xl p-12 w-full max-w-[400px]">
      <h1 class="m-0 text-[2rem] font-semibold text-white text-center mb-2">Connexion</h1>
      <p class="text-center text-white/60 mb-8">Accéder au dashboard</p>

      <form @submit.prevent="handleLogin" class="flex flex-col gap-6">
        <AppInput 
          id="email" 
          type="email" 
          label="Email"
          v-model="email" 
          placeholder="votre@email.com"
          required
          autocomplete="email"
          class="!bg-black/20 !border-white/10 !rounded-lg !py-3 !px-4"
        />

        <AppInput 
          id="password" 
          :type="isPasswordVisible ? 'text' : 'password'" 
          label="Mot de passe"
          v-model="password" 
          placeholder="••••••••"
          required
          autocomplete="current-password"
          class="!bg-black/20 !border-white/10 !rounded-lg !py-3 !pl-4 !pr-13"
        >
          <template #suffix>
            <button 
              type="button" 
              @click="togglePasswordVisibility"
              class="absolute right-3 top-[38px] text-white/50 hover:text-white focus:outline-none transition-colors flex items-center justify-center w-8 h-8"
              aria-label="Afficher ou masquer le mot de passe"
            >
              <svg v-if="!isPasswordVisible" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                <line x1="1" y1="1" x2="23" y2="23"></line>
              </svg>
            </button>
          </template>
        </AppInput>

        <AppButton type="submit" variant="outline" :loading="isLoading" class="p-4 rounded-lg w-full mt-2">
          <span v-if="isLoading">Connexion en cours...</span>
          <span v-else>Se connecter</span>
        </AppButton>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth.service'
import { toast } from '@/composables/useToast'
import AppInput from '@/components/Showcase/ui/AppInput.vue'
import AppButton from '@/components/Showcase/ui/AppButton.vue'

const router = useRouter()
const email = ref('')
const password = ref('')
const isLoading = ref(false)
const isPasswordVisible = ref(false)

const togglePasswordVisibility = () => {
  isPasswordVisible.value = !isPasswordVisible.value
}

const handleLogin = async () => {
  if (!email.value || !password.value) {
    toast.error('Veuillez remplir tous les champs')
    return
  }

  isLoading.value = true

  try {
    const success = await authService.login(email.value, password.value)
    if (success) {
      router.push('/dashboard')
    } else {
      toast.error('Identifiants incorrects')
    }
  } catch (e) {
    toast.error(e, 'Une erreur est survenue lors de la connexion')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>

.btn-return {
  transition: all 0.3s ease;
}
.btn-return:hover {
  color: var(--color-teal);
  text-shadow: 0 0 10px var(--color-teal);
  border-color: color-mix(in srgb, var(--color-teal) 50%, transparent);
  background-color: color-mix(in srgb, var(--color-black) 80%, transparent);
}

.login-card {
  box-shadow: 0 0 30px color-mix(in srgb, var(--color-teal) 10%, transparent);
}

.input-field {
  transition: all 0.3s ease;
}
.input-field:focus {
  box-shadow: 0 0 10px color-mix(in srgb, var(--color-teal) 20%, transparent);
}
</style>
