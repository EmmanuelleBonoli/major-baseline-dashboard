import { ref, type Ref } from 'vue'
import { enableAnalytics, disableAnalytics } from '@/services/firebase'

/** Choix de l'utilisateur sur les cookies de mesure d'audience. */
export type ConsentStatus = 'granted' | 'denied'

interface StoredConsent {
  status: ConsentStatus
  date: string
  version: number
}

const CONSENT_KEY = 'mb-cookie-consent'

// À incrémenter si la politique de confidentialité évolue de façon
// significative : force le réaffichage du bandeau à tous les visiteurs.
const CONSENT_VERSION = 1

const hasStorage = typeof window !== 'undefined' && typeof localStorage !== 'undefined'

const status: Ref<ConsentStatus | null> = ref(null)
const isBannerOpen = ref(false)

/**
 * Relit le choix mémorisé. Retourne null si absent, illisible ou issu d'une
 * version de politique antérieure (auquel cas il faut redemander).
 */
function readStored(): StoredConsent | null {
  if (!hasStorage) return null
  try {
    const raw = localStorage.getItem(CONSENT_KEY)
    if (!raw) return null
    const parsed = JSON.parse(raw) as StoredConsent
    if (parsed.version !== CONSENT_VERSION) return null
    if (parsed.status !== 'granted' && parsed.status !== 'denied') return null
    return parsed
  } catch {
    return null
  }
}

/**
 * Persiste le choix. En cas d'échec (navigation privée, quota) on n'échoue
 * pas : le bandeau réapparaîtra simplement à la prochaine visite.
 */
function persist(next: ConsentStatus): void {
  if (!hasStorage) return
  try {
    const payload: StoredConsent = {
      status: next,
      date: new Date().toISOString(),
      version: CONSENT_VERSION
    }
    localStorage.setItem(CONSENT_KEY, JSON.stringify(payload))
  } catch {
    /* stockage indisponible : on ignore volontairement */
  }
}

/**
 * Gestion centralisée du consentement cookies (mesure d'audience uniquement).
 * Le token d'authentification du dashboard reste hors périmètre : c'est un
 * traceur strictement nécessaire, exempté de consentement.
 */
export function useConsent() {
  /** L'utilisateur accepte : on mémorise et on active l'analytics. */
  function accept(): void {
    status.value = 'granted'
    isBannerOpen.value = false
    persist('granted')
    enableAnalytics()
  }

  /** L'utilisateur refuse : on mémorise et on coupe tout suivi. */
  function refuse(): void {
    status.value = 'denied'
    isBannerOpen.value = false
    persist('denied')
    disableAnalytics()
  }

  /** Rouvre le bandeau (lien « Gérer les cookies » du footer). */
  function openBanner(): void {
    isBannerOpen.value = true
  }

  /**
   * À appeler une seule fois au démarrage côté client : applique le choix
   * mémorisé, ou ouvre le bandeau si aucun choix valide n'existe.
   */
  function applyStoredConsent(): void {
    const stored = readStored()
    if (!stored) {
      status.value = null
      isBannerOpen.value = true
      return
    }
    status.value = stored.status
    if (stored.status === 'granted') enableAnalytics()
  }

  return { status, isBannerOpen, accept, refuse, openBanner, applyStoredConsent }
}
