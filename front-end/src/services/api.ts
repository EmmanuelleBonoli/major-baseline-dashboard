import axios from 'axios'

export const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('majorBaselineToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
