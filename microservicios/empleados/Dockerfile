# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
