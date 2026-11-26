# Order Service

## Overview
Order Service is a Spring Boot microservice responsible for order-related operations.

## Technology Stack
- Java 21
- Spring Boot
- Maven
- Docker

## Health Check
GET /orders/health

## Docker Build
docker build -t order-service:1.0 .

## Docker Run
docker run -d -p 18083:18082 order-service:1.0
