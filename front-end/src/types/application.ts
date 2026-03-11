export interface ShowcaseApp {
  id: string
  title: string
  tagline: string
  icon: string
  category: string[]
  status: string
  publishDate?: string // YYYY-MM-DD
  description?: string
  images?: string[] // screenshot / illustrations
}
