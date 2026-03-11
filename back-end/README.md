# Backend - Major Baseline Dashboard

API REST Spring Boot pour le suivi des statistiques d'applications mobiles et des jeux du studio Major Baseline.

## 🏗️ Architecture

```
backend/
├── src/main/java/com/appdashboard/
│   ├── AppStatsDashboardApplication.java    # Point d'entrée
│   ├── features/
│   │   ├── Application/                     # Gestion des Projets globaux (Jeux, Apps)
│   │   │   ├── Application.java
│   │   │   ├── ApplicationDTO.java
│   │   │   ├── ApplicationRepository.java
│   │   │   └── ApplicationService.java
│   │   ├── Store/                           # Gestion des versions par Plateforme (Android, iOS)
│   │   │   ├── Store.java
│   │   │   ├── StoreDTO.java
│   │   │   ├── StoreRepository.java
│   │   │   ├── StoreService.java
│   │   │   ├── GooglePlayService.java       # Intégration Google Play
│   │   │   └── AppleAppStoreService.java    # Intégration App Store
│   │   ├── AppStats/                        # Statistiques de chaque Store
│   │   │   ├── AppStats.java
│   │   │   ├── AppStatsService.java
│   │   │   └── StatsDTO.java
│   │   ├── Dashboard/                       # API et Agrégation
│   │   │   ├── DashboardController.java
│   │   │   ├── DashboardService.java
│   │   │   └── AppDashboardDTO.java
│   │   ├── Contact/                         # Formulaire de contact public
│   │   │   ├── ContactController.java
│   │   │   └── ContactService.java
│   │   ├── User/                            # Utilisateurs du tableau de bord
│   │   │   └── UserService.java
│   │   ├── Authentication/                  # Login
│   │   └── config/, security/, exception/   # Core et configuration (Swagger, JWT, etc.)
└── src/main/resources/
    ├── application.yml                      # Configuration principale
    ├── application-development.yml          # Config développement
    ├── application-staging.yml              # Config staging
    └── application-production.yml           # Config production
```

## 🔧 Technologies

- **Spring Boot 3.4.0** - Framework principal
- **Spring Data JPA** - ORM / Persistence
- **MySQL 8.0** - Base de données
- **Lombok** - Réduction du boilerplate
- **JJWT (io.jsonwebtoken >= 0.11)** - Sécurité avec JSON Web Tokens
- **Springdoc OpenAPI (Swagger)** - Documentation des API
- **Google API Client** - Intégration Google Play

## 🔄 Système de cache & Synchronisation

L'`AppStatsService` implémente un **cache intelligent** qui :

1. Vérifie si des données existent pour la période et le store demandés.
2. Vérifie si les données sont récentes (jusqu'à J-1).
3. Vérifie la couverture de la période (> 80% de données disponibles).
4. Fetch automatiquement les points manquants auprès des API externes (Google Play Console, App Store Connect) lorsque nécessaire.

## 🚀 Lancement

### En développement

```bash
# Avec Maven (MySQL en local ou Docker tourne et le .env est rempli)
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### En production

```bash
# Build 
mvn clean package

# Run
java -jar target/dashboard-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

## 🧪 Tests et Analyse

```bash
# Lancer les tests
mvn test

# Générer une API Doc (Swagger) avec OpenAPI
# Une fois le serveur lancé, naviguer sur : http://localhost:8080/swagger-ui/index.html
```
