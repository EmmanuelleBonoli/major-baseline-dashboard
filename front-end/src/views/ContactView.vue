<template>
  <div class="contact-page">
    <div class="bg-gradient-decoration"></div>
    <div class="bg-grid-decoration"></div>
    <HeaderSection />
    <div class="w-full shrink-0 h-20 md:h-28 lg:h-32"></div>
    <div class="form-wrapper z-10">
      <div class="form-card w-full max-w-[700px] bg-black/80 border border-teal/30 p-8 md:p-14 backdrop-blur-md relative">
        <div class="mb-6 md:mb-10 border-b border-teal/20 pb-5">
          <h1 class="form-title text-[2.5rem] md:text-[4rem] font-black uppercase text-white tracking-[2px] md:tracking-[4px] leading-tight m-0">
            Contact
          </h1>
          <p class="text-gold tracking-[1px] md:tracking-[2px] uppercase text-[0.8rem] md:text-[1rem] mt-2 font-bold">
            /// Initialiser la transmission ///
          </p>
        </div>

        <form @submit.prevent="handleSubmit" novalidate class="flex flex-col gap-1 md:gap-2">
          <AppInput
            v-model="email"
            type="email"
            label="Adresse Email"
            placeholder="votre@email.com"
            animated-dot
            :errorMessage="errors.email"
            @blur="validateField('email')"
            @input="clearError('email')"
          />

          <AppInput
            v-model="subject"
            type="text"
            label="Intitulé du message"
            placeholder="Objet de votre demande..."
            animated-dot
            animation-delay="0.25s"
            :errorMessage="errors.subject"
            @blur="validateField('subject')"
            @input="clearError('subject')"
          />

          <AppInput
            v-model="body"
            textarea
            rows="5"
            label="Corps de transmission"
            placeholder="Décrivez ici pourquoi vous me contactez, votre besoin, votre contexte..."
            animated-dot
            animation-delay="0.5s"
            :errorMessage="errors.body"
            @blur="validateField('body')"
            @input="clearError('body')"
          />

          <div class="w-full flex justify-end mt-4">
            <AppButton type="submit" variant="primary" :loading="isLoading" class="btn-submit px-8 py-4">
              <span>{{ isLoading ? 'Envoi en cours...' : 'Envoyer la requête' }}</span>
            </AppButton>
          </div>
        </form>
      </div>
    </div>

    <FooterSection class="contact-footer" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { contactApi } from '@/services/contact.service'
import { toast } from '@/composables/useToast'
import { useParticles } from '@/composables/useParticles'
import AppInput from '@/components/Showcase/ui/AppInput.vue'
import AppButton from '@/components/Showcase/ui/AppButton.vue'
import HeaderSection from '@/components/Showcase/HeaderSection.vue'
import FooterSection from '@/components/Showcase/FooterSection.vue'

useParticles()

const email = ref('')
const subject = ref('')
const body = ref('')
const isLoading = ref(false)

const errors = reactive({
  email: '',
  subject: '',
  body: ''
})

const clearError = (field: 'email' | 'subject' | 'body') => {
  errors[field] = ''
}

const validateField = (field: 'email' | 'subject' | 'body') => {
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
    case 'subject':
      if (!subject.value.trim()) {
        errors.subject = "L'intitulé du message est requis."
      } else if (subject.value.trim().length < 3) {
        errors.subject = "L'intitulé doit contenir au moins 3 caractères."
      } else {
        errors.subject = ''
      }
      break
    case 'body':
      if (!body.value.trim()) {
        errors.body = "Le corps du message est requis."
      } else if (body.value.trim().length < 10) {
        errors.body = "Le message doit contenir au moins 10 caractères."
      } else {
        errors.body = ''
      }
      break
  }
}

const validateAll = (): boolean => {
  validateField('email')
  validateField('subject')
  validateField('body')
  return !errors.email && !errors.subject && !errors.body
}

const handleSubmit = async () => {
  if (!validateAll()) return

  isLoading.value = true

  try {
    await contactApi.sendMessage(email.value, subject.value, body.value)
    toast.success('Votre message a bien été transmis. Nous vous répondrons dans les plus brefs délais !')
    email.value = ''
    subject.value = ''
    body.value = ''
    errors.email = ''
    errors.subject = ''
    errors.body = ''
  } catch (error) {
    toast.error(error, "Une erreur est survenue lors de l'envoi de votre message. Veuillez réessayer plus tard.")
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.contact-page {
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

.form-wrapper {
  position: relative;
  width: 100%;
  flex: 1;
  padding: 4rem 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-card {
  box-shadow: 0 0 40px color-mix(in srgb, var(--color-teal) 10%, transparent);
}
.form-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-image: linear-gradient(to bottom, var(--color-teal), transparent);
}

.form-title {
  text-shadow: 0 0 15px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.btn-submit {
  clip-path: polygon(0 0, 90% 0, 100% 30%, 100% 100%, 10% 100%, 0 70%);
}

.contact-footer {
  width: 100vw;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
}

@keyframes gridScroll {
  0% { transform: translateY(0); }
  100% { transform: translateY(50px); }
}
</style>
