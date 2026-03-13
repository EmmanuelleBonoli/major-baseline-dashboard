FROM node:20-alpine AS build

WORKDIR /app

# Layer séparé pour les dépendances (cache Docker)
COPY package*.json ./
RUN npm ci --silent

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
