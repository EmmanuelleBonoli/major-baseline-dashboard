import type { ShowcaseApp } from '@/types/application'

export const alasso: ShowcaseApp = {
  id: 'alasso',
  title: "À l'Asso",
  icon: '🤝',
  tagline: "Site de partage d'activités au service des associations",
  status: 'Disponible',
  category: ['Plateforme Web', 'Associatif'],
  description: `
Site de partage d'activités au service des associations,
réalisé dans le cadre de la formation Wild Code School.
  `,
  images: ['/images/alasso.png'],
  techStack: ['Angular', 'Typescript', 'Spring', 'Java', 'Hibernate', 'Docker', 'CI/CD'],
  links: {
    github: ['https://github.com/WildCodeSchool/a-lasso_frontend', 'https://github.com/WildCodeSchool/a-lasso_backend']
  }
}
