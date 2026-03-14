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

export function useSEO(options: SEOOptions = {}) {
  const route = useRoute()
  const siteName = 'Major Baseline'
  const defaultDescription =
    "Major Baseline est un studio spécialisé dans le développement d'applications et de jeux mobiles. Découvrez nos créations!"
  const defaultKeywords =
    "jeux mobiles, développement d'applications, studio de jeux, portfolio, capacitor, flutter, react native, vuejs, spring boot, java, typescript, emmanuelle bonoli, fullstack developer, software engineer, développeuse, développeur, fullstack, game developer, applications mobiles, jeux indépendants"

  const isPublic = computed(() => route?.meta?.requiresAuth === false)

  const title = computed(() => {
    const t = unref(options.title)
    return t ? `${t} | ${siteName}` : siteName
  })

  const description = computed(() => {
    return unref(options.description) || defaultDescription
  })

  const keywords = computed(() => {
    return unref(options.keywords) || defaultKeywords
  })

  const type = computed(() => {
    return unref(options.type) || 'website'
  })

  const image = computed(() => {
    return unref(options.image) || '/og-image.png'
  })

  const structuredData = computed(() => {
    if (!isPublic.value) return null
    return {
      '@context': 'https://schema.org',
      '@type': 'LocalBusiness',
      name: siteName,
      description: defaultDescription,
      url: 'https://major-baseline.com',
      logo: 'https://major-baseline.com/major-baseline-logo-dark.svg',
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
    title,
    link: [{ rel: 'canonical', href: `https://major-baseline.com${route.path}` }],
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
      ogTitle: title,
      ogDescription: description,
      ogImage: image,
      ogType: type,
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
