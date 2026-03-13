<template>
  <div class="login-page">
    <div class="bg-gradient-decoration"></div>
    <div class="bg-grid-decoration"></div>

    <HeaderSection />

    <div class="w-full shrink-0 h-20 md:h-28 lg:h-32"></div>

    <div class="login-wrapper z-10">
      <div class="login-card w-full max-w-[480px] bg-black/80 border border-teal/30 p-8 md:p-14 backdrop-blur-md relative">

        <div class="mb-8 border-b border-teal/20 pb-5">
          <h1 class="card-title text-[2rem] md:text-[3rem] font-black uppercase text-white tracking-[2px] md:tracking-[4px] leading-tight m-0">
            Connexion
          </h1>
          <p class="text-gold tracking-[1px] md:tracking-[2px] uppercase text-[0.75rem] md:text-[0.9rem] mt-2 font-bold">
            /// Accès dashboard ///
          </p>
        </div>

        <form @submit.prevent="handleLogin" novalidate class="flex flex-col gap-1 md:gap-2">
          <AppInput
            id="login-email"
            type="email"
            label="Email"
            v-model="email"
            placeholder="votre@email.com"
            autocomplete="email"
            animated-dot
            :errorMessage="errors.email"
            @blur="validateField('email')"
            @input="clearError('email')"
          />

          <AppInput
            id="login-password"
            :type="isPasswordVisible ? 'text' : 'password'"
            label="Mot de passe"
            v-model="password"
            placeholder="••••••••"
            autocomplete="current-password"
            animated-dot
            animation-delay="0.25s"
            :errorMessage="errors.password"
            @blur="validateField('password')"
            @input="clearError('password')"
          >
            <template #suffix>
              <button
                type="button"
                @click="togglePasswordVisibility"
                class="cursor-pointer absolute right-3 top-1/2 -translate-y-1/2 text-white/40 hover:text-teal focus:outline-none transition-colors flex items-center justify-center w-8 h-8"
                aria-label="Afficher ou masquer le mot de passe"
              >
                <svg
                  v-if="!isPasswordVisible"
                  xmlns="http://www.w3.org/2000/svg"
                  width="18"
                  height="18"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
                <svg
                  v-else
                  xmlns="http://www.w3.org/2000/svg"
                  width="18"
                  height="18"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                  <line x1="1" y1="1" x2="23" y2="23"></line>
                </svg>
              </button>
            </template>
          </AppInput>

          <div class="w-full flex justify-end mt-4">
            <AppButton type="submit" variant="primary" :loading="isLoading" class="btn-login px-8 py-4">
              <span>{{ isLoading ? 'Connexion...' : 'Accéder au dashboard' }}</span>
            </AppButton>
          </div>
        </form>
      </div>
    </div>

    <FooterSection class="login-footer" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth.service'
import { toast } from '@/composables/useToast'
import { useParticles } from '@/composables/useParticles'
import AppInput from '@/components/Showcase/ui/AppInput.vue'
import AppButton from '@/components/Showcase/ui/AppButton.vue'
import HeaderSection from '@/components/Showcase/HeaderSection.vue'
import FooterSection from '@/components/Showcase/FooterSection.vue'

useParticles()

const router = useRouter()
const email = ref('')
const password = ref('')
const isLoading = ref(false)
const isPasswordVisible = ref(false)

const errors = reactive({
  email: '',
  password: ''
})

const clearError = (field: 'email' | 'password') => {
  errors[field] = ''
}

const validateField = (field: 'email' | 'password') => {
  switch (field) {
    case 'email':
      if (!email.value.trim()) {
        errors.email = "L'adresse email est requise."
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
        errors.email = "Veuillez saisir une adresse email valide."
      } else {
        errors.email = ''
      }
      break
    case 'password':
      if (!password.value) {
        errors.password = 'Le mot de passe est requis.'
      } else {
        errors.password = ''
      }
      break
  }
}

const validateAll = (): boolean => {
  validateField('email')
  validateField('password')
  return !errors.email && !errors.password
}

const togglePasswordVisibility = () => {
  isPasswordVisible.value = !isPasswordVisible.value
}

const handleLogin = async () => {
  if (!validateAll()) return

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
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
  position: relative;
  background-color: var(--color-deep-blue);
}

.bg-gradient-decoration {
  position: fixed;
  inset: 0;
  z-index: 0;
  background-image:
    radial-gradient(ellipse at top right, color-mix(in srgb, var(--color-teal) 15%, transparent) 0%, transparent 50%),
    radial-gradient(ellipse at bottom left, color-mix(in srgb, var(--color-gold) 10%, transparent) 0%, transparent 50%);
}

.bg-grid-decoration {
  position: fixed;
  inset: 0;
  z-index: 0;
  opacity: 0.3;
  background-image:
    linear-gradient(color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px),
    linear-gradient(90deg, color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridScroll 20s linear infinite;
}

.login-wrapper {
  position: relative;
  width: 100%;
  flex: 1;
  padding: 3rem 1rem 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  box-shadow: 0 0 40px color-mix(in srgb, var(--color-teal) 10%, transparent);
}


.login-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-image: linear-gradient(to bottom, var(--color-teal), transparent);
}

.card-title {
  text-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.btn-login {
  clip-path: polygon(0 0, 90% 0, 100% 30%, 100% 100%, 10% 100%, 0 70%);
}
.login-footer {
  width: 100vw;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
}

@keyframes gridScroll {
  0%  { transform: translateY(0); }
  100%{ transform: translateY(50px); }
}
</style>
