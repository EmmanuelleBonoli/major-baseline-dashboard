<template>
  <div class="min-h-screen text-white flex flex-col items-center justify-center p-4 md:p-8 bg-deep-blue relative overflow-hidden">
    <!-- Background Decoration -->
    <div class="bg-gradient-decoration fixed top-0 left-0 w-full h-full z-0"></div>
    <div class="bg-grid-decoration fixed top-0 left-0 w-full h-full z-0 opacity-30"></div>

    <!-- Header -->
    <div class="absolute top-0 w-full left-0 p-4 md:p-8 z-20">
      <router-link to="/" class="btn-back inline-flex items-center gap-2 text-teal uppercase tracking-[1px] md:tracking-[3px] font-bold py-2 md:py-3 px-4 md:px-6 text-sm md:text-base border-2 border-teal bg-black/40 transition-all duration-300">
        &lt; Retour au site
      </router-link>
    </div>

    <!-- Form Container -->
    <div class="form-card w-full max-w-[700px] bg-black/80 border border-teal/30 p-8 md:p-14 backdrop-blur-md relative mt-16 md:mt-0 z-10">
      
      <div class="mb-8 md:mb-12 border-b border-teal/20 pb-6">
        <h1 class="form-title text-[2.5rem] md:text-[4rem] font-black uppercase text-white tracking-[2px] md:tracking-[4px] leading-tight m-0">Contact</h1>
        <p class="text-gold tracking-[1px] md:tracking-[2px] uppercase text-[0.8rem] md:text-[1rem] mt-2 font-bold">/// Initialiser la transmission ///</p>
      </div>

      <form @submit.prevent="handleSubmit" class="flex flex-col gap-6 md:gap-8">
        <AppInput 
          v-model="email" 
          type="email" 
          label="Adresse Email"
          placeholder="votre@email.com"
          required
          animated-dot
        />

        <AppInput 
          v-model="subject" 
          type="text" 
          label="Intitulé du message"
          placeholder="Objet de votre demande..."
          required
          animated-dot
          animation-delay="0.25s"
        />

        <AppInput 
          v-model="body" 
          textarea
          rows="6" 
          label="Corps de transmission"
          placeholder="Entrez vos coordonnées et décrivez votre projet ici..."
          required
          animated-dot
          animation-delay="0.5s"
        />

        <div class="w-full flex justify-end mt-4">
          <AppButton 
            type="submit" 
            variant="primary"
            :loading="isLoading"
            class="btn-submit px-8 py-4"
          >
            <span>{{ isLoading ? 'Envoi en cours...' : 'Envoyer la requête' }}</span>
            <span class="text-xl" v-if="!isLoading">🚀</span>
          </AppButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { contactApi } from '@/services/contact.service'
import { toast } from '@/composables/useToast'
import AppInput from '@/components/Showcase/ui/AppInput.vue'
import AppButton from '@/components/Showcase/ui/AppButton.vue'

const email = ref('')
const subject = ref('')
const body = ref('')

const isLoading = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const handleSubmit = async () => {
  isLoading.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    await contactApi.sendMessage(email.value, subject.value, body.value)
    toast.success('Votre message a bien été transmis. Nous vous répondrons dans les plus brefs délais !')
    email.value = ''
    subject.value = ''
    body.value = ''
  } catch (error) {
    toast.error(error, 'Une erreur est survenue lors de l\'envoi de votre message. Veuillez réessayer plus tard.')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.bg-deep-blue {
  background-color: var(--color-deep-blue);
}

.bg-gradient-decoration {
  background-image: 
    radial-gradient(ellipse at top right, color-mix(in srgb, var(--color-teal) 15%, transparent) 0%, transparent 50%),
    radial-gradient(ellipse at bottom left, color-mix(in srgb, var(--color-gold) 10%, transparent) 0%, transparent 50%);
}

.bg-grid-decoration {
  background-image: 
    linear-gradient(color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px),
    linear-gradient(90deg, color-mix(in srgb, var(--color-teal) 10%, transparent) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridScroll 20s linear infinite;
}

.btn-back {
  clip-path: polygon(0 0, 85% 0, 100% 30%, 100% 100%, 15% 100%, 0 70%);
}
.btn-back:hover {
  background-color: var(--color-teal);
  color: var(--color-black);
  box-shadow: 0 0 20px var(--color-teal);
}

.form-card {
  box-shadow: 0 0 40px color-mix(in srgb, var(--color-teal) 10%, transparent);
}
.form-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px; /* w-1 */
  height: 100%;
  background-image: linear-gradient(to bottom, var(--color-teal), transparent);
}

.form-title {
  text-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.btn-submit {
  clip-path: polygon(0 0, 90% 0, 100% 30%, 100% 100%, 10% 100%, 0 70%);
}

@keyframes gridScroll {
  0% { transform: translateY(0); }
  100% { transform: translateY(50px); }
}
</style>
