Coffee Order Processing Service
===============================

Overview
--------

Spring Boot microservice for processing coffee shop orders with queue management.

Features
--------

*   Order creation with queue assignment
*   Order status tracking
*   Queue position monitoring
*   Customer loyalty tracking
*   Shop capacity management

Tech Stack
----------

*   Java 17
*   Spring Boot 3.x
*   PostgreSQL
*   Liquibase (database migrations)
*   Swagger/OpenAPI (documentation)
*   Docker

Setup
-----

### Prerequisites

*   Java 17 JDK
*   PostgreSQL 15+
*   Maven 3.8+

### Installation

1.  Clone repository
2.  Configure database in `application.yml`:

        spring:
          datasource:
            url: jdbc:postgresql://localhost:5432/coffee_orders
            username: postgres
            password: yourpassword

3.  Run the application:

        mvn spring-boot:run


API Documentation
-----------------

Swagger UI available at:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Endpoints
---------

*   `POST /api/v1/orders` - Create new order
*   `GET /api/v1/orders/{id}` - Get order status
*   `DELETE /api/v1/orders/{id}` - Cancel order
*   `GET /api/v1/orders/queue/{shopId}` - Get queue status

Database
--------

Schema is managed via Liquibase migrations in:  
`src/main/resources/db/changelog/`

Deployment
----------

    mvn clean package
    java -jar target/coffee-order-service.jar

Contact
-------

Uditha - [udithanayanajith97@gmail.com](mailto:udithanayanajith97@gmail.com)