import { initializeApp } from 'firebase/app'
import { getAnalytics, logEvent } from 'firebase/analytics'

const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID,
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
  measurementId: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID
}

// Initialisation de Firebase
const app = initializeApp(firebaseConfig)

// Initialisation de l'analytique seulement si nous sommes en production ou si measurementId est fourni
let analytics: any = null
if (typeof window !== 'undefined' && firebaseConfig.measurementId) {
  analytics = getAnalytics(app)
}

/**
 * Enregistre un événement de vue de page
 * @param page_path Le chemin de la page
 * @param page_title Le titre de la page
 */
export const trackPageView = (page_path: string, page_title: string) => {
  if (analytics) {
    logEvent(analytics, 'page_view', {
      page_path,
      page_title
    })
  }
}

/**
 * Enregistre un événement personnalisé
 * @param eventName Nom de l'événement
 * @param params Paramètres de l'événement
 */
export const trackEvent = (eventName: string, params: object = {}) => {
  if (analytics) {
    logEvent(analytics, eventName, params)
  }
}

/**
 * Enregistre une exception/erreur
 * @param description Description de l'erreur
 * @param fatal Indique si l'erreur est fatale
 */
export const trackException = (description: string, fatal: boolean = false) => {
  if (analytics) {
    logEvent(analytics, 'exception', {
      description,
      fatal
    })
  }
}

export { app, analytics }
