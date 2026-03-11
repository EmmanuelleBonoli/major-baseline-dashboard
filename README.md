# 🎮 Game Studio Dashboard

Dashboard fullstack de gestion et de suivi des statistiques d'un studio de jeux mobiles indépendant.

## Stack

| Couche | Technologie |
|---|---|
| **Frontend** | Vue 3 · TypeScript · Tailwind CSS v4 · Vite |
| **Backend** | Spring Boot 3 · Spring Data JPA · Spring Security · MySQL 8 |
| **Auth** | JWT (JJWT) |
| **Qualité** | Husky · Prettier · Checkstyle · GitHub Actions |

## Structure du projet

```
Game_Studio_Dashboard/
├── front-end/          # Application Vue 3
├── back-end/           # API REST Spring Boot
├── .github/workflows/  # CI/CD (lint + tests back & front)
├── .husky-scripts/     # Scripts de pre-commit
├── .prettierrc         # Config Prettier partagée (Java + Vue/TS)
└── docker-compose.yml  # Base de données MySQL locale
```

## Démarrage rapide

### Prérequis
- Node.js ≥ 20
- Java 23
- Maven 3.9+
- MySQL 8 (ou Docker)

### 1. Base de données
```bash
docker-compose up -d
```

### 2. Backend
```bash
cd back-end
mvn spring-boot:run -Dspring-boot.run.profiles=development
# API disponible sur http://localhost:8080
# Swagger UI : http://localhost:8080/swagger-ui/index.html
```

### 3. Frontend
```bash
cd front-end
npm install
npm run dev
# Application sur http://localhost:5173
```

## Qualité du code

Les hooks Git vérifient automatiquement avant chaque commit :
- **Checkstyle** sur le code Java
- **Prettier** sur Java, Vue et TypeScript
- **Build** du frontend

```bash
# Installer les hooks (une seule fois)
npm install

# Lancer Prettier manuellement
npm run prettier
```

## CI/CD

Le pipeline GitHub Actions (`.github/workflows/main.yml`) s'exécute sur chaque PR et vérifie l'ensemble du projet (back + front).

Les branches suivent le flux : `feature/*` → `development` → `staging` → `main`
