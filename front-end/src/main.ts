import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createHead } from '@unhead/vue/client'
import App from './App.vue'
import './style.css'
import router from './router'

const app = createApp(App)
const head = createHead()
const pinia = createPinia()

app.use(head)
app.use(pinia)
app.use(router)
app.mount('#app')
