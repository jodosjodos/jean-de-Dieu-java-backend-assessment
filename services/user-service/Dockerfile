FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy parent POM and common module first
COPY ../../pom.xml ./pom.xml
COPY ../../shared/common-utils ./shared/common-utils

# Copy service POM and source
COPY pom.xml ./services/user-service/pom.xml
COPY src ./services/user-service/src

# Build the application
WORKDIR /app/services/user-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/services/user-service/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
