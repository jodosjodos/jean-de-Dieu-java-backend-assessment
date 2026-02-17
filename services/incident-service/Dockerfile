FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy parent POM and common module first
COPY ../../pom.xml ./pom.xml
COPY ../../shared/common-utils ./shared/common-utils

# Copy service POM and source
COPY pom.xml ./services/incident-service/pom.xml
COPY src ./services/incident-service/src

# Build the application
WORKDIR /app/services/incident-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/services/incident-service/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
