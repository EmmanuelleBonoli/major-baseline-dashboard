import type { ShowcaseApp } from '@/types/application'

export const moodflow: ShowcaseApp = {
  id: 'moodflow',
  title: 'Moodflow',
  icon: '🧠',
  tagline: "Génère un planning personnalisé en fonction de votre humeur grâce à l'IA",
  status: 'en développement',
  order: 4,
  category: ['Productivité', 'IA'],
  description: `
Application type todolist qui génère un planning personnalisé des tâches
en fonction de l'humeur de l'utilisateur grâce à l'IA.
  `,
  images: ['/images/moodflow.webp'],
  techStack: ['Next.js', 'Typescript', 'Nest.js', 'Prisma', 'IA', 'Docker', 'CI/CD'],
  links: {
    github: ['https://github.com/EmmanuelleBonoli/Moodflow']
  }
}
