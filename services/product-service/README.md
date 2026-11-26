# Product Service

## Overview
Product Service is a Spring Boot microservice in the CI/CD platform.

## Technology Stack
- Java 21
- Spring Boot
- Maven
- Docker

## Health Check
GET /products/health

## Docker Build
docker build -t product-service:1.0 .

## Docker Run
docker run -d -p 18081:18081 product-service:1.0
