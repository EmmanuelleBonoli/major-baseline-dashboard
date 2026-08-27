import type { ShowcaseApp } from '@/types/application'

export const wordRiders: ShowcaseApp = {
  id: 'wordRiders',
  icon: '️🚀',
  title: 'Word Riders',
  tagline: 'Course à haute vitesse ',
  category: ['Racing', 'Word Puzzle'],
  status: 'Sortie Q3 2026',
  publishDate: '2026-08-01',
  description: `
Word Riders est un jeu de puzzle de mots mobile. 
Les joueurs doivent former des mots en utilisant les lettres fournies 
pour avancer sur la piste et faire la course jusqu'à la ligne d'arrivée !
  `,
  techStack: ['Flutter', 'Dart', 'Flame', 'Firebase'],
  policies: {
    hasCGV: true,
    hasPrivacyPolicy: true
  }
}
