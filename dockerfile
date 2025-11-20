# --------- Build Stage ---------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar solo el pom primero para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY . .
RUN mvn clean package -DskipTests

# --------- Runtime Stage ---------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar JAR desde la build
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Seteamos el profile por defecto para contenedores
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]
