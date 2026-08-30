/// <reference types="vite-ssg/node" />
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'
import { writeFileSync } from 'node:fs'
import { fileURLToPath, URL } from 'node:url'
import { buildPublicRoutes, buildSitemapXml, buildRobotsTxt } from './src/content/prerenderRoutes'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())
  const siteUrl = (env.VITE_SITE_URL || 'https://major-baseline.fr').replace(/\/+$/, '')

  return {
    plugins: [
      vue(),
      tailwindcss(),
      {
        // Remplace le jeton %SITE_URL% de index.html par l'URL résolue.
        name: 'inject-site-url',
        transformIndexHtml: (html: string) => html.replaceAll('%SITE_URL%', siteUrl)
      }
    ],
    // Garantit que import.meta.env.VITE_SITE_URL est toujours défini côté app
    // (même valeur que le sitemap / robots / index.html).
    define: {
      'import.meta.env.VITE_SITE_URL': JSON.stringify(siteUrl)
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    server: {
      port: 3000,
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true
        }
      }
    },
    // Pré-rendu statique des pages publiques (vite-ssg).
    // dirStyle 'nested' -> /contact/index.html, compatible avec le try_files nginx.
    ssgOptions: {
      script: 'async',
      formatting: 'minify',
      dirStyle: 'nested',
      includedRoutes: () => buildPublicRoutes(),
      // sitemap.xml et robots.txt régénérés à chaque build depuis la même source.
      onFinished() {
        const toDist = (file: string) => fileURLToPath(new URL(`./dist/${file}`, import.meta.url))
        writeFileSync(toDist('sitemap.xml'), buildSitemapXml(siteUrl), 'utf-8')
        writeFileSync(toDist('robots.txt'), buildRobotsTxt(siteUrl), 'utf-8')
      }
    }
  }
})
