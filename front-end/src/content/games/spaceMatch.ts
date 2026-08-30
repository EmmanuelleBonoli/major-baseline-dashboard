import type { ShowcaseApp } from '@/types/application'

export const spaceMatch: ShowcaseApp = {
  id: 'spaceMatch',
  title: 'Space Match',
  icon: '🧩',
  tagline: 'Un jeu mobile dans le style Puyo Puyo',
  status: '1er semestre 2027',
  order: 3,
  category: ['Puzzle', 'Mobile'],
  description: `
Jeu mobile du style Puyo Puyo.
  `,
  images: ['/images/spaceMatch.webp'],
  techStack: ['Vue.js', 'Typescript', 'Capacitor', 'Docker', 'CI/CD'],
  links: {
    github: ['https://github.com/EmmanuelleBonoli/Space-Match']
  }
}
