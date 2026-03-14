import { createRouter, createWebHistory } from 'vue-router'
import { useHead, useSeoMeta } from '@unhead/vue'
import ApplicationsView from '@/views/ApplicationsView.vue'
import AppDashboardView from '@/views/AppDashboardView.vue'
import LoginView from '@/views/LoginView.vue'
import ShowcaseView from '@/views/ShowcaseView.vue'
import ContactView from '@/views/ContactView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'showcase',
      component: ShowcaseView,
      meta: {
        requiresAuth: false,
        title: 'Accueil',
        description:
          "Major Baseline - Studio de création d'applications et jeux mobiles. Découvrez l'ensemble de nos créations!"
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
        title: 'Connexion',
        description:
          "Connexion privée au dashboard de Major Baseline, permet d'accéder aux statistiques et gérer les projets du studio."
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
  ],
  scrollBehavior(to, from, savedPosition) {
    void from
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth'
      }
    }
    return savedPosition || { top: 0 }
  }
})

router.afterEach((to) => {
  const siteName = 'Major Baseline'
  const title = to.meta.title ? `${to.meta.title} | ${siteName}` : siteName
  const description = (to.meta.description as string) || ''
  const isPublic = to.meta.requiresAuth === false

  useHead({ title })

  if (isPublic) {
    useSeoMeta({
      title,
      description,
      ogTitle: title,
      ogDescription: description,
      ogType: 'website'
    })
  } else {
    useSeoMeta({
      description: '',
      ogTitle: '',
      ogDescription: '',
      robots: 'noindex, nofollow'
    })
  }
})

router.beforeEach((to, from, next) => {
  void from
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

export default router
