FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copie du POM en premier pour profiter du cache des layers (dépendances)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copie du code source
COPY src ./src
COPY checkstyle.xml ./checkstyle.xml

# Build sans les tests (ils tournent en CI)
RUN mvn clean package -DskipTests -q

# ─── Image finale — JRE seul, image légère ─────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
