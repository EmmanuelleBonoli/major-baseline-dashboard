import type { ShowcaseApp } from '@/types/application'

export const writer: ShowcaseApp = {
  id: 'writer',
  title: 'Writer',
  icon: '✍️',
  tagline: "L'atelier d'écriture de roman, de la bible du monde au manuscrit exporté",
  status: '1er semestre 2027',
  order: 2,
  category: ['Écriture', 'IA'],
  description: `
Application mobile et web (iOS, Android, Web) d'aide à l'écriture de roman.
Un livre = un projet indépendant : bible du monde (ton, pitch, thèmes, règles),
fiches personnages et lieux, timeline des événements regroupés par arcs narratifs,
puis rédaction des scènes reliées aux personnages, lieux et événements, et
regroupées en chapitres.

Import d'un document Word existant, dictée vocale, et réécriture assistée par IA
(Gemini via un Worker Cloudflare) qui réécrit un passage ou complète un champ vide
à partir du contexte.
  `,
  techStack: ['Expo', 'React Native', 'TypeScript', 'Zustand', 'Cloudflare Workers', 'Gemini'],
  links: {
    github: ['https://github.com/EmmanuelleBonoli/writer']
  },
  images: ['/images/writer.webp']
}
