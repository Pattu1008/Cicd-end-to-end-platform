FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/product-service-1.0.0.jar app.jar
EXPOSE 18081
ENTRYPOINT ["java", "-jar", "app.jar"]
