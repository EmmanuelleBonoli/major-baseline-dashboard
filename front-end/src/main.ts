import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createHead } from '@unhead/vue/client'
import App from './App.vue'
import './style.css'
import router from './router'
import { trackException } from './services/firebase'

const app = createApp(App)

// Gestionnaire d'erreurs global pour Firebase Analytics
app.config.errorHandler = (err: any, _instance: any, info: string) => {
    console.error('Vue Error:', err, info)
    trackException(`${err.message} (${info})`, true)
}

const head = createHead()
const pinia = createPinia()

app.use(head)
app.use(pinia)
app.use(router)
app.mount('#app')
