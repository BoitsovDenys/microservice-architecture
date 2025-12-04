# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy the Gradle configuration files
COPY settings.gradle.kts gradlew /app/
COPY gradle /app/gradle
COPY song-service/build.gradle.kts /app/song-service/

# Download dependencies
RUN chmod +x ./gradlew && ./gradlew :resource-service:dependencies --no-daemon

# Copy source code
COPY song-service/src /app/song-service/src

# Build the application
RUN ./gradlew :song-service:assemble -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/song-service/build/libs/*.jar app.jar

# Expose port
EXPOSE 8082

# Run command
CMD ["java", "-jar", "app.jar"]
