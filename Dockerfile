# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
WORKDIR /app/nomad-score
RUN ./mvnw package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/nomad-score/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
