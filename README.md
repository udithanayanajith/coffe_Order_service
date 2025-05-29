Coffee Order Processing Service
===============================

Overview
--------

Spring Boot microservice for processing coffee shop orders with queue management.
This service handles order processing, status tracking, cancellation, and queue management for coffee shops with multiple queues.

Description
--------
When a customer places an order, the system first validates that both the customer and shop are registered in the database. It then checks if the shop is currently open by verifying the current time against the shop's operating hours. The system confirms the requested queue number is valid for that location and ensures the queue isn't at full capacity. If all validations pass, it calculates the customer's position in the queue (current queue length + 1) and saves the order while incrementing the customer's loyalty score by 1 point. For status checks, the system retrieves the order details and calculates a 2-minute wait time per queue position. Order cancellation is only permitted for orders with a "WAITING" status. The queue status feature provides real-time queue length and wait time estimates (2 minutes per order) for each shop's queues. Throughout this process, the system enforces business rules including valid customer/shop registration, operating hour compliance, queue capacity limits, and status-based cancellation policies.

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