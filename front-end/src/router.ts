import { createRouter, createWebHistory } from 'vue-router'
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
      meta: { requiresAuth: false }
    },
    {
      path: '/project/:id',
      name: 'project-presentation',
      component: () => import('@/views/AppPresentationView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/contact',
      name: 'contact',
      component: ContactView,
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/dashboard',
      name: 'stores',
      component: ApplicationsView,
      meta: { requiresAuth: true, layout: 'dashboard' }
    },
    {
      path: '/application/:id',
      name: 'application-dashboard',
      component: () => import('@/views/AppDashboardView.vue'),
      meta: { requiresAuth: true, layout: 'dashboard' }
    },
    {
      path: '/application/:id/store/:storeId',
      name: 'store-dashboard',
      component: AppDashboardView,
      meta: { requiresAuth: true, layout: 'dashboard' }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      redirect: '/'
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    void from;
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth',
      }
    }
    return savedPosition || { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  void from;
  const isAuthenticated = !!localStorage.getItem('majorBaselineToken')

  if (to.matched.some(record => record.meta.requiresAuth)) {
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
