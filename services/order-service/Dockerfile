FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/order-service-1.0.0.jar app.jar
EXPOSE 18082
ENTRYPOINT ["java", "-jar", "app.jar"]
