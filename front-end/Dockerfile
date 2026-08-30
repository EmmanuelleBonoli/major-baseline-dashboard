FROM node:20-alpine AS build

WORKDIR /app

# Layer séparé pour les dépendances (cache Docker)
COPY package*.json ./
RUN npm ci --silent

# URL de production injectée au build : le pré-rendu vite-ssg fige les URLs
# absolues (canonical, Open Graph, sitemap, JSON-LD) dans le HTML statique.
# Valeur par défaut = prod ; surchargée via --build-arg en CI ou par front-end/.env
# quand il est présent dans le contexte (npm run docker:dev).
ARG VITE_SITE_URL=https://major-baseline.fr
ENV VITE_SITE_URL=$VITE_SITE_URL

# Code source + build production
COPY . .
RUN npm run build

# ─── Image finale — Nginx alpine ───────────────────────────────────────────
FROM nginx:alpine

# Config Nginx (SPA routing + proxy vers backend)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Artefacts de build
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
