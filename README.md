# streamshop

## Overview

StreamShop is a microservices-based e-commerce platform built with Spring Boot and Java 21. The application follows a distributed architecture pattern where each service is responsible for a specific business domain.

## Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│             │     │             │     │             │
│   Clients   │────▶│ API Gateway │────▶│  Services   │
│             │     │             │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
                           │
                           │
          ┌───────────────┬┴──────────────┬───────────┐
          ▼               ▼               ▼           ▼
┌─────────────────┐ ┌───────────────┐ ┌─────────┐ ┌─────────────┐
│ Product Service │ │ Order Service │ │User Svc │ │Inventory Svc│
└─────────────────┘ └───────────────┘ └─────────┘ └─────────────┘
                            │
                            ▼                      
                    ┌─────────────────┐
                    │Notification Svc │
                    └─────────────────┘                          
```

All requests pass through the API Gateway which handles authentication and routes requests to appropriate microservices.

## Microservices

- **API Gateway:** Entry point for all client requests with OAuth2 security
- **Product Service:** Manages product catalog and information
- **Inventory Service:** Handles stock and inventory management
- **Order Service:** Processes and manages customer orders
- **User Service:** Manages user accounts and authentication
- **Notification Service:** Handles notifications to users

## Tech Stack

- **Java:** Version 21
- **Spring Boot:** Version 3.5.0
- **Spring Cloud:** Version 2025.0.0
- **Database:** SQL (specific implementation details not provided)
- **API Documentation:** OpenAPI/Swagger
- **Authentication:** OAuth2 Resource Server
- **Build Tool:** Maven
- **Containerization:** Docker

## Building and Running

### Prerequisites

- Java 21
- Maven
- Docker

### Build Commands

```sh
# Clone the repository
git clone git@github.com:Sundhar22/streamshop.git

# Build all services
cd streamshop
mvn clean package

# Build docker images
mvn spring-boot:build-image

# Run with Docker Compose (if available)
docker-compose up
```

## Configuration

Each service has its own specific configuration:

- **API Gateway:** Configured with OAuth2 for authentication and Spring Cloud Gateway for routing
- **Product/Inventory Services:** Include OpenAPI configuration for API documentation

## Docker Configuration

Docker images are automatically published to Docker Hub with the following configuration:

```xml
<image>
  <name>mohanasundharam/${project.artifactId}</name>
  <publish>true</publish>
</image>
<docker>
  <publishRegistry>
    <username>mohanasundharam</username>
    <password>${docker-password}</password>
  </publishRegistry>
</docker>
```

## Development
The project follows a modular structure with each microservice having its own codebase, configuration, and API documentation.
