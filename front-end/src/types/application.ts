export interface ShowcaseApp {
  id: string
  title: string
  tagline: string
  icon: string
  category: string[]
  status: string
  order?: number // ordre d'affichage manuel dans la liste des projets (croissant)
  publishDate?: string // YYYY-MM-DD
  description?: string
  images?: string[] // screenshot / illustrations
  techStack?: string[] // technologies used
  policies?: {
    hasCGV?: boolean
    hasPrivacyPolicy?: boolean
  }
  links?: {
    github?: string[]
    live?: string
  }
}
