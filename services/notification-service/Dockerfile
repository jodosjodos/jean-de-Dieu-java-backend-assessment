FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY ../../pom.xml ./pom.xml
COPY ../../shared/common-utils ./shared/common-utils
COPY pom.xml ./services/notification-service/pom.xml
COPY src ./services/notification-service/src
WORKDIR /app/services/notification-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/services/notification-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
