import { ViteSSG } from 'vite-ssg'
import { createPinia } from 'pinia'
import App from './App.vue'

// Polices auto-hébergées
// Importées avant style.css pour que le bundler les résolve avant les @font-face du thème.
import '@fontsource-variable/inter/wght.css'
import '@fontsource-variable/space-grotesk/wght.css'
import '@fontsource/rajdhani/latin-300.css'
import '@fontsource/rajdhani/latin-400.css'
import '@fontsource/rajdhani/latin-500.css'
import '@fontsource/rajdhani/latin-600.css'
import '@fontsource/rajdhani/latin-700.css'

import './style.css'
import { routes, scrollBehavior, registerRouterGuards } from './router'
import { trackException } from './services/firebase'
import { useConsent } from './composables/useConsent'

// Entrée compatible pré-rendu : vite-ssg crée l'app, le router et le head,
// puis exécute ce callback aussi bien au build (SSG) que côté client.
export const createApp = ViteSSG(App, { routes, scrollBehavior }, ({ app, router }) => {
  app.use(createPinia())
  registerRouterGuards(router)

  // Client uniquement : consentement cookies + gestionnaire d'erreurs global.
  if (!import.meta.env.SSR) {
    // Applique le choix cookies mémorisé, ou ouvre le bandeau si aucun.
    useConsent().applyStoredConsent()

    app.config.errorHandler = (err: any, _instance: any, info: string) => {
      console.error('Vue Error:', err, info)
      trackException(`${err.message} (${info})`, true)
    }
  }
})
