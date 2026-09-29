# Multi-stage Dockerfile for Automated Public Transport Feedback Portal
# Stage 1: Build & Package application using Maven and JDK 17
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# Cache Maven dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy sources and package WAR
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Minimal Eclipse Temurin JRE runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Non-root user for security
RUN groupadd -r ptfp && useradd -r -g ptfp -d /app ptfp

COPY --from=builder /build/target/ptfp.war /app/ptfp.war
RUN chown -R ptfp:ptfp /app

USER ptfp
EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/ptfp.war"]
