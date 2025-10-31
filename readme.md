# Spring Boot and JWT

A simple Spring Boot application demonstrating JWT (JSON Web Token) authentication using Aspect-Oriented Programming (AOP).

## Overview

This project showcases how to implement JWT-based authentication in a Spring Boot application using AOP. The application intercepts all REST controller method calls and validates the JWT token provided in the request header.

## Technologies Used

- Java 21
- Spring Boot 3.2.10
- Spring AOP
- JJWT (JSON Web Token for Java) 0.11.5
- JUnit 5 for testing
- Maven for dependency management

## Features

- JWT token generation and validation
- AOP-based authentication for all REST endpoints
- Simple Hello World REST API

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/edwin/spring-boot-and-jwt.git
cd spring-boot-and-jwt
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080 by default.

## API Endpoints

### Hello World Endpoint

```
GET /
```

Returns a simple "Hello World!" message in JSON format.

Example response:
```json
{
  "message": "Hello World!"
}
```

Note: All endpoints require a valid JWT token in the request header.

## Authentication

All REST endpoints in this application are protected by JWT authentication. To access any endpoint, you need to include a valid JWT token in the request header.

### JWT Token Header

Include the JWT token in the `my_token` header of your HTTP request:

```
my_token: <your_jwt_token>
```

### JWT Token Format

The JWT token contains the following claims:
- Subject: Username
- Custom claim "username": Username
- Issued At: Token creation time
- Expiration: Token expiration time (1 hour after creation by default)

### Token Expiration

- Access tokens expire after 1 hour (3600000 milliseconds)
- Refresh tokens expire after 24 hours (86400000 milliseconds)

## Testing

Run the tests using Maven:

```bash
mvn test
```

The project includes comprehensive tests for JWT token generation and validation.

## Configuration

The application configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=spring-boot-and-jwt
server.port=8080

# logging
logging.level.root=INFO
logging.level.com.edw=DEBUG
```

## Author

Muhammad Edwin