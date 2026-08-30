import { useHead, useSeoMeta } from '@unhead/vue'
import { computed, unref, type Ref } from 'vue'
import { useRoute } from 'vue-router'

export interface SEOOptions {
  title?: string | Ref<string>
  description?: string | Ref<string>
  image?: string | Ref<string>
  type?: 'website' | 'article' | Ref<'website' | 'article'>
  keywords?: string | Ref<string>
  schema?: any
}

// URL de production du site, sans slash final. Sert de base pour les URLs
// absolues (canonical, Open Graph, JSON-LD) exigées par les crawlers.
export const SITE_URL = (import.meta.env.VITE_SITE_URL as string).replace(/\/+$/, '')

/**
 * Transforme un chemin racine ("/og-image.png") en URL absolue.
 * Les balises Open Graph / Twitter et le JSON-LD sont ignorés par la plupart
 * des scrapers sociaux s'ils ne reçoivent pas une URL absolue.
 */
export function toAbsoluteUrl(path: string): string {
  if (/^https?:\/\//.test(path)) return path
  return `${SITE_URL}${path.startsWith('/') ? '' : '/'}${path}`
}

/**
 * Centralise toute la gestion des métadonnées SEO d'une page publique :
 * title, description, canonical, Open Graph, Twitter Card et données
 * structurées JSON-LD. À appeler dans le setup de chaque vue publique.
 *
 * En l'absence d'option explicite, les valeurs sont reprises depuis
 * `route.meta` (source de vérité unique définie dans router.ts).
 */
export function useSEO(options: SEOOptions = {}) {
  const route = useRoute()
  const siteName = 'Major Baseline'
  const defaultDescription =
    "Major Baseline est un studio spécialisé dans le développement d'applications et de jeux mobiles. Découvrez nos créations!"
  const defaultKeywords =
    "jeux mobiles, développement d'applications, studio de jeux, portfolio, capacitor, flutter, react native, vuejs, spring boot, java, typescript, emmanuelle bonoli, fullstack developer, software engineer, développeuse, développeur, fullstack, game developer, applications mobiles, jeux indépendants"

  const isPublic = computed(() => route?.meta?.requiresAuth === false)

  const canonicalUrl = computed(() => `${SITE_URL}${route.path}`)

  const title = computed(() => {
    const t = unref(options.title) ?? (route?.meta?.title as string | undefined)
    return t ? `${t} | ${siteName}` : siteName
  })

  const description = computed(() => {
    return unref(options.description) || (route?.meta?.description as string | undefined) || defaultDescription
  })

  const keywords = computed(() => {
    return unref(options.keywords) || defaultKeywords
  })

  const type = computed(() => {
    return unref(options.type) || 'website'
  })

  const image = computed(() => {
    return toAbsoluteUrl(unref(options.image) || '/og-image.png')
  })

  // Données structurées présentes sur toutes les pages publiques : l'entité
  // studio (Organization) et le site lui-même (WebSite), reliées par @id.
  // Les pages projet ajoutent leur propre bloc via l'option `schema`.
  const structuredData = computed(() => {
    if (!isPublic.value) return null
    return {
      '@context': 'https://schema.org',
      '@graph': [
        {
          '@type': 'Organization',
          '@id': `${SITE_URL}/#organization`,
          name: siteName,
          url: SITE_URL,
          description: defaultDescription,
          logo: {
            '@type': 'ImageObject',
            url: `${SITE_URL}/major-baseline-logo-dark.svg`
          },
          sameAs: ['https://github.com/EmmanuelleBonoli', 'https://www.linkedin.com/in/emmanuellebonoli'],
          founder: {
            '@type': 'Person',
            name: 'Emmanuelle Bonoli',
            jobTitle: 'Fullstack Developer & Game Studio Founder',
            sameAs: ['https://github.com/EmmanuelleBonoli', 'https://www.linkedin.com/in/emmanuellebonoli']
          },
          knowsAbout: [
            'Capacitor',
            'Flutter',
            'React Native',
            'Vue.js',
            'Spring Boot',
            'Java',
            'TypeScript',
            'Game Development',
            'Software Architecture',
            'Firebase'
          ]
        },
        {
          '@type': 'WebSite',
          '@id': `${SITE_URL}/#website`,
          url: SITE_URL,
          name: siteName,
          description: defaultDescription,
          inLanguage: 'fr-FR',
          publisher: { '@id': `${SITE_URL}/#organization` }
        }
      ]
    }
  })

  const scripts = computed(() => {
    const items = []
    if (structuredData.value) {
      items.push({
        type: 'application/ld+json',
        innerHTML: JSON.stringify(structuredData.value)
      })
    }
    const customSchema = unref(options.schema)
    if (customSchema) {
      items.push({
        type: 'application/ld+json',
        innerHTML: JSON.stringify(customSchema)
      })
    }
    return items
  })

  useHead({
    htmlAttrs: { lang: 'fr' },
    title,
    link: [{ rel: 'canonical', href: canonicalUrl }],
    meta: [
      { name: 'keywords', content: keywords },
      { name: 'author', content: 'Emmanuelle Bonoli' }
    ],
    script: scripts
  })

  if (isPublic.value) {
    useSeoMeta({
      title,
      description,
      keywords,
      robots: 'index, follow',
      ogSiteName: siteName,
      ogTitle: title,
      ogDescription: description,
      ogImage: image,
      ogType: type,
      ogUrl: canonicalUrl,
      ogLocale: 'fr_FR',
      twitterCard: 'summary_large_image',
      twitterTitle: title,
      twitterDescription: description,
      twitterImage: image
    })
  } else {
    useSeoMeta({
      description: '',
      robots: 'noindex, nofollow',
      ogTitle: '',
      ogDescription: ''
    })
  }
}
