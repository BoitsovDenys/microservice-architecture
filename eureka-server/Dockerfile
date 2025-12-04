# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy the Gradle configuration files
COPY settings.gradle.kts gradlew /app/
COPY gradle /app/gradle
COPY eureka-server/build.gradle.kts /app/eureka-server/

# Download dependencies
RUN chmod +x ./gradlew && ./gradlew :eureka-server:dependencies --no-daemon

# Copy source code
COPY eureka-server/src /app/eureka-server/src

# Build the application
RUN ./gradlew :eureka-server:assemble -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/eureka-server/build/libs/*.jar app.jar

# Expose port
EXPOSE 8761

# Run command
CMD ["java", "-jar", "app.jar"]
