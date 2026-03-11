# Frontend — Game Studio Dashboard

Interface Vue 3 pour visualiser les statistiques d'applications mobiles du studio.

## Stack

- **Vue 3** + **TypeScript** + **Vite**
- **Tailwind CSS v4** — styles utilitaires, thème centralisé dans `style.css`
- **Vue Router** — routing SPA
- **Axios** — client HTTP
- **Chart.js** + **vue-chartjs** — graphiques
- **Lucide Vue Next** — icônes SVG

## Architecture

```
src/
├── views/              # Pages (ShowcaseView, LoginView, ContactView, ApplicationsView...)
├── components/
│   ├── Showcase/       # Sections de la vitrine publique
│   ├── Dashboard/      # Composants du dashboard privé
│   ├── ui/             # Composants réutilisables (AppButton, AppInput)
│   └── common/         # Composants globaux (AppToast)
├── layouts/
│   └── DashboardLayout.vue   # Layout sidebar + contenu
├── services/
│   ├── dashboard.api.service.ts  # Appels HTTP purs
│   ├── dashboard.service.ts      # Logique métier
│   ├── auth.service.ts
│   └── contact.service.ts
├── composables/        # Hooks Vue réutilisables
├── content/            # Données statiques (jeux, applications)
├── router/
└── style.css           # Thème global Tailwind (@theme, variables CSS)
```

## Lancement

```bash
npm install
npm run dev       # http://localhost:5173
npm run build     # Build production
```

Le proxy Vite redirige automatiquement `/api/*` vers `http://localhost:8080`.

## Convention de style

Les styles complexes (gradients, clip-path, animations) sont extraits en classes CSS dans les blocs `<style scoped>` de chaque composant. Les couleurs et ombres passent par des variables CSS définies dans `style.css` :

```css
@theme {
  --color-teal: #1a8b9d;
  --color-gold: #d4af37;
  --shadow-glow-teal: ...;
}
```
