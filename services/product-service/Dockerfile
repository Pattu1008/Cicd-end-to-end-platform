
# Stage 1: Prepare application artifact
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY target/product-service-1.0.0.jar app.jar

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 18081

ENTRYPOINT ["java", "-jar", "app.jar"]
