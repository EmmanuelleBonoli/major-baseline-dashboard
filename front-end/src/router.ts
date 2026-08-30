import type { Router, RouteRecordRaw, RouterScrollBehavior } from 'vue-router'
import { useHead, useSeoMeta } from '@unhead/vue'
import ApplicationsView from '@/views/ApplicationsView.vue'
import AppDashboardView from '@/views/AppDashboardView.vue'
import LoginView from '@/views/LoginView.vue'
import ShowcaseView from '@/views/ShowcaseView.vue'
import ContactView from '@/views/ContactView.vue'
import { trackPageView } from '@/services/firebase'

// L'instance de router (et son history) est créée par vite-ssg à partir de ces
// options : memory history au pré-rendu, web history côté client.
export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'showcase',
    component: ShowcaseView,
    meta: {
      requiresAuth: false,
      title: "Studio de développement d'applications et de jeux mobiles",
      description:
        "Major Baseline - Studio de développement d'applications et jeux mobiles. Découvrez l'ensemble de nos créations!"
    }
  },
  {
    path: '/project/:id',
    name: 'project-presentation',
    component: () => import('@/views/AppPresentationView.vue'),
    meta: {
      requiresAuth: false,
      title: 'Projet',
      description: 'Découvrez les détails des créations du studio.'
    }
  },
  {
    path: '/contact',
    name: 'contact',
    component: ContactView,
    meta: {
      requiresAuth: false,
      title: 'Contact',
      description:
        "Une question ? Un projet ? Contactez Major Baseline et sa fondatrice pour toute demande de collaboration ou d'information."
    }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false,
      noindex: true,
      title: 'Connexion',
      description:
        "Connexion privée au dashboard de Major Baseline, permet d'accéder aux statistiques et gérer les projets du studio."
    }
  },
  {
    path: '/policies/:projectId/:type',
    name: 'policy',
    component: () => import('@/views/PolicyView.vue'),
    meta: {
      requiresAuth: false,
      title: 'Document Légal',
      description: 'Document Légal (CGV ou Politique de Confidentialité) du projet sélectionné'
    }
  },
  {
    path: '/dashboard',
    name: 'stores',
    component: ApplicationsView,
    meta: {
      requiresAuth: true,
      layout: 'dashboard',
      title: 'Dashboard'
    }
  },
  {
    path: '/dashboard/application/:id',
    name: 'application-dashboard',
    component: () => import('@/views/AppDashboardView.vue'),
    meta: {
      requiresAuth: true,
      layout: 'dashboard',
      title: 'App Dashboard'
    }
  },
  {
    path: '/dashboard/application/:id/store/:storeId',
    name: 'store-dashboard',
    component: AppDashboardView,
    meta: {
      requiresAuth: true,
      layout: 'dashboard',
      title: 'Détails Store'
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    redirect: '/'
  }
]

export const scrollBehavior: RouterScrollBehavior = (to, from, savedPosition) => {
  void from
  if (to.hash) {
    return {
      el: to.hash,
      behavior: 'smooth'
    }
  }
  return savedPosition || { top: 0 }
}

/**
 * Attache les guards de navigation au router créé par vite-ssg.
 * Le code dépendant du navigateur (auth via localStorage, tracking Firebase)
 * est court-circuité pendant le pré-rendu (`import.meta.env.SSR`).
 */
export function registerRouterGuards(router: Router): void {
  router.afterEach((to) => {
    const siteName = 'Major Baseline'
    const title = to.meta.title ? `${to.meta.title} | ${siteName}` : siteName

    // Une page est indexable si elle est publique ET non marquée `noindex`.
    // Les pages publiques indexables gèrent leurs métadonnées SEO complètes
    // via le composable useSEO (appelé dans le setup de chaque vue).
    const isIndexable = to.meta.requiresAuth === false && to.meta.noindex !== true

    if (!isIndexable) {
      useHead({ title })
      useSeoMeta({
        description: '',
        ogTitle: '',
        ogDescription: '',
        robots: 'noindex, nofollow'
      })
    }

    if (!import.meta.env.SSR) {
      trackPageView(to.fullPath, title)
    }
  })

  router.beforeEach((to, from, next) => {
    void from

    // Pas d'authentification pendant le pré-rendu : aucune route protégée
    // n'est pré-rendue, on laisse simplement passer.
    if (import.meta.env.SSR) {
      next()
      return
    }

    const isAuthenticated = !!localStorage.getItem('majorBaselineToken')

    if (to.matched.some((record) => record.meta.requiresAuth)) {
      if (!isAuthenticated) {
        next({ name: 'login' })
      } else {
        next()
      }
    } else {
      // Redirection vers le dashboard si déjà connecté et tentative d'accès à la page de connexion
      if (to.name === 'login' && isAuthenticated) {
        next({ name: 'stores' })
      } else {
        next()
      }
    }
  })
}
