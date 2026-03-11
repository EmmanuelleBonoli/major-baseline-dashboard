<template>
  <div id="app">
    <component :is="layout">
      <router-view />
    </component>
    <AppToast />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import DashboardLayout from '@/layouts/DashboardLayout.vue'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import AppToast from '@/components/common/AppToast.vue'

const route = useRoute()

const layout = computed(() => {
  return route.meta.layout === 'dashboard' ? DashboardLayout : DefaultLayout
})
</script>

<style scoped></style>
