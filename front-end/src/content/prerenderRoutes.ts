/**
 * Routes publiques du site : pré-rendu HTML statique (vite-ssg), sitemap et
 * robots.txt sont tous dérivés d'ici.
 *
 * Ce fichier ne doit contenir AUCUN import (il est chargé par vite.config.ts) :
 * garder la liste synchronisée manuellement avec src/content/applications et
 * src/content/games.
 */

export const PROJECT_IDS = ['moodflow', 'alasso', 'astroPuyo', 'wordRiders'] as const

export const STATIC_PUBLIC_ROUTES = ['/', '/contact'] as const

/** Toutes les routes publiques indexables, au format chemin absolu. */
export function buildPublicRoutes(): string[] {
  return [...STATIC_PUBLIC_ROUTES, ...PROJECT_IDS.map((id) => `/project/${id}`)]
}

/**
 * Contenu XML du sitemap, dérivé de buildPublicRoutes().
 * @param siteUrl domaine de production sans slash final.
 * @param lastmod date ISO (YYYY-MM-DD) ; par défaut la date du build.
 */
export function buildSitemapXml(siteUrl: string, lastmod: string = new Date().toISOString().slice(0, 10)): string {
  const entries = buildPublicRoutes().map((path) => {
    const priority = path === '/' ? '1.0' : path.startsWith('/project/') ? '0.8' : '0.6'
    const changefreq = path === '/' ? 'weekly' : 'monthly'
    return [
      '  <url>',
      `    <loc>${siteUrl}${path}</loc>`,
      `    <lastmod>${lastmod}</lastmod>`,
      `    <changefreq>${changefreq}</changefreq>`,
      `    <priority>${priority}</priority>`,
      '  </url>'
    ].join('\n')
  })
  return `<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n${entries.join('\n')}\n</urlset>\n`
}

/**
 * Contenu du robots.txt. Autorise tout sauf l'espace privé et la page de
 * connexion, et pointe vers le sitemap.
 * @param siteUrl domaine de production sans slash final.
 */
export function buildRobotsTxt(siteUrl: string): string {
  return [
    'User-agent: *',
    'Allow: /',
    'Disallow: /dashboard/',
    'Disallow: /login',
    '',
    `Sitemap: ${siteUrl}/sitemap.xml`,
    ''
  ].join('\n')
}
