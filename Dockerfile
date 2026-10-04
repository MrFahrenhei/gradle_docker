FROM gradle:9.0.0-jdk21-alpine AS builder
WORKDIR /build

COPY build.gradle.kts settings.gradle.kts /build/
RUN gradle dependencies --no-daemon

COPY src /build/src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S system_group && adduser -S system_user -G system_group
USER system_user:system_group

COPY --from=builder /build/build/libs/*.jar /app/app.jar
COPY --from=builder /home/gradle/.gradle/caches/modules-2/files-2.1/org.postgresql/postgresql/42.7.13/**/*.jar /app/libs/

COPY .env /app/.env

EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app/app.jar:/app/libs/*", "DatabaseConnector"]