import { initializeApp } from 'firebase/app'
import { getAnalytics, logEvent, type Analytics } from 'firebase/analytics'

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

// L'analytique n'est JAMAIS initialisée au chargement : elle attend le
// consentement explicite de l'utilisateur (RGPD / recommandations CNIL).
// Tant que `analytics` vaut null, toutes les fonctions de suivi sont inertes.
let analytics: Analytics | null = null

// Nom du drapeau d'opt-out officiel de Google Analytics pour ce flux.
const gaDisableFlag = `ga-disable-${firebaseConfig.measurementId}`

/**
 * Active Google Analytics après recueil du consentement.
 * Idempotent : sans effet si déjà actif, hors navigateur ou sans measurementId.
 */
export const enableAnalytics = (): void => {
  if (analytics) return
  if (typeof window === 'undefined' || !firebaseConfig.measurementId)
    return // Lève un éventuel opt-out posé par un refus précédent dans la même session.
  ;(window as unknown as Record<string, boolean>)[gaDisableFlag] = false
  analytics = getAnalytics(app)

  // Vue de page initiale : l'utilisateur a consenti après le chargement,
  // le guard de router ne rejouera pas la navigation courante.
  if (typeof document !== 'undefined') {
    logEvent(analytics, 'page_view', {
      page_path: window.location.pathname + window.location.search,
      page_title: document.title
    })
  }
}

/**
 * Désactive Google Analytics (refus initial ou changement de choix).
 * Un GA déjà chargé ne peut être totalement retiré du runtime : on neutralise
 * les envois via le drapeau `ga-disable-<measurementId>` et on repasse
 * `analytics` à null pour rendre les fonctions de suivi inertes.
 */
export const disableAnalytics = (): void => {
  analytics = null
  if (typeof window === 'undefined' || !firebaseConfig.measurementId) return
  ;(window as unknown as Record<string, boolean>)[gaDisableFlag] = true
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
