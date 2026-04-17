# Build stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render sets the PORT environment variable automatically (default 10000)
# Spring Boot can use this if mapped, but exposing 8080 is standard.
EXPOSE 8080

# Run the application (Render provides the PORT environment variable)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
