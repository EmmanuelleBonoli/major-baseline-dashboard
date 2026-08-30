import type { ShowcaseApp } from '@/types/application'

export const astroPuyo: ShowcaseApp = {
  id: 'astroPuyo',
  title: 'Astro Puyo',
  icon: '🧩',
  tagline: 'Un jeu mobile dans le style Puyo Puyo',
  status: 'Disponible',
  category: ['Puzzle', 'Mobile'],
  description: `
Jeu mobile du style Puyo Puyo.
  `,
  images: ['/images/astro-puyo.webp'],
  techStack: ['Vue.js', 'Typescript', 'Spring', 'Java', 'Hibernate', 'Capacitor', 'Docker', 'CI/CD'],
  links: {
    github: [
      'https://github.com/EmmanuelleBonoli/puyo-reboot-front',
      'https://github.com/EmmanuelleBonoli/puyo-reboot-back'
    ]
  }
}
