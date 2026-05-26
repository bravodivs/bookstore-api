<img width="1376" height="768" alt="bookstore-cover" src="https://github.com/user-attachments/assets/6cd97b5d-401d-41b1-9b06-6452389e0311" />

# Bookstore Microservices Workspace

This project is a production-style Bookstore platform built using a microservices architecture with Spring Boot.

The goal of the project is not just to build CRUD APIs, but to simulate how large-scale e-commerce systems are designed in real-world environments.

The system is designed around:
- Domain-driven microservices
- Event-driven communication using Kafka
- Independent databases per service
- API Gateway + Service Discovery
- Scalable and loosely coupled services
- Distributed workflows across orders, inventory, payments, shipping, and notifications

The platform evolves phase-by-phase:
- Phase 1 → Core bookstore
- Phase 2 → Shopping experience
- Phase 3 → Operations and logistics
- Phase 4 → Customer engagement
- Phase 5 → Enterprise scalability and analytics


## Included Modules

- `eureka-server` (service registry)
- `config-server` (centralized config)
- `api-gateway` (single client entry point)
- `auth-service`
- `catalog-service`
- `inventory-service`
- `pricing-service`
- `cart-service`
- `order-service`
- `payment-service`
- `shipping-service`
- `review-service`
- `recommendation-service`
- `notification-service`
- `analytics-service`

## Local Infrastructure

The `infra/docker-compose.yml` stack provisions:

- Kafka + Zookeeper
- PostgreSQL
- Redis
- MongoDB
- Elasticsearch

Run from repository root:

```bash
docker compose -f infra/docker-compose.yml up -d
```

## Database Initialization

`infra/init-scripts/init.sql` creates the Postgres databases used by the services:

- `authdb`
- `catalogdb`
- `inventorydb`
- `pricingdb`
- `orderdb`
- `paymentsdb`
- `shippingdb`
- `reviewdb`

## Suggested Startup Order

1. `eureka-server`
2. `config-server`
3. `api-gateway`
4. Business services (`auth-service`, `catalog-service`, `inventory-service`, `pricing-service`, `cart-service`, `order-service`, `payment-service`, `shipping-service`, `review-service`, `recommendation-service`, `notification-service`, `analytics-service`)

Each module can be started independently with:

```bash
mvn spring-boot:run
```

## Full Local Run (End-to-End)

Start infrastructure:

```bash
docker compose -f infra/docker-compose.yml up -d
```

Start services in separate terminals (from repo root):

```bash
sh eureka-server/mvnw -f eureka-server/pom.xml spring-boot:run
sh config-server/mvnw -f config-server/pom.xml spring-boot:run
sh api-gateway/mvnw -f api-gateway/pom.xml spring-boot:run
sh catalog-service/mvnw -f catalog-service/pom.xml spring-boot:run
sh inventory-service/mvnw -f inventory-service/pom.xml spring-boot:run
sh order-service/mvnw -f order-service/pom.xml spring-boot:run
sh payment-service/mvnw -f payment-service/pom.xml spring-boot:run
sh shipping-service/mvnw -f shipping-service/pom.xml spring-boot:run
sh notification-service/mvnw -f notification-service/pom.xml spring-boot:run
sh analytics-service/mvnw -f analytics-service/pom.xml spring-boot:run
```

## Happy Path API Flow

1. Create a book in catalog:

```bash
curl -X POST http://localhost:8080/api/catalog/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn":"9780132350884",
    "title":"Clean Code",
    "description":"A Handbook of Agile Software Craftsmanship",
    "price":499.00
  }'
```

2. Add stock for that `bookId`:

```bash
curl -X POST http://localhost:8080/api/inventory/add/<bookId>/10
```

3. Place an order:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "bookId":"<bookId>",
    "quantity":1,
    "unitPrice":499.00,
    "userEmail":"buyer@example.com"
  }'
```

4. Check order state:

```bash
curl http://localhost:8080/api/orders/<orderId>
```

The order transitions through:
- `PENDING_PAYMENT` after order creation
- `PAID` after payment event
- `SHIPPED` after shipping event



# High-Level Architecture

## Infrastructure Services

### API Gateway
Single entry point for all client applications.

Responsibilities:
- Request routing
- JWT validation
- Rate limiting
- CORS handling
- Request logging
- Distributed tracing propagation

Tech Stack:
- Spring Cloud Gateway

---

### Service Registry
Provides service discovery.

Responsibilities:
- Dynamic registration of services
- Service lookup
- Load balancing support

Tech Stack:
- Eureka Server / Consul

---

### Config Server
Centralized configuration management.

Responsibilities:
- Shared configuration storage
- Environment-specific configs
- Dynamic config refresh

Tech Stack:
- Spring Cloud Config
- Git-backed configuration repository

---

# Core Business Services

## Auth Service
Responsible for authentication and authorization.

Responsibilities:
- User signup/login
- JWT token generation
- Role-based access control
- Session management

Database:
- PostgreSQL

Published Events:
- UserCreated
- UserUpdated

Consumed By:
- Notification Service
- Analytics Service

---

## Catalog Service
Public-facing book catalog service.

Responsibilities:
- Manage books, authors, publishers
- Search and filtering
- Provide catalog APIs for frontend
- Maintain denormalized read-optimized views

Database:
- MongoDB / PostgreSQL
- Elasticsearch for full-text search

Published Events:
- BookCreated
- BookUpdated

Consumes:
- StockUpdated
- PriceChanged
- ReviewCreated

Interactions:
- Reads inventory availability
- Reads pricing information
- Reads aggregated review ratings

---

## Inventory Service
Source of truth for stock management.

Responsibilities:
- Manage stock quantity
- Reserve stock during checkout
- Release stock on order cancellation/payment failure
- Track warehouse inventory

Database:
- PostgreSQL

Published Events:
- StockUpdated
- StockReserved
- StockReleased

Consumes:
- BookCreated
- OrderCancelled

Interactions:
- Order Service reserves inventory
- Catalog Service updates availability status

---

## Pricing Service
Responsible for all pricing logic.

Responsibilities:
- Manage book prices
- Promotions and discounts
- Tax calculations
- Regional pricing

Database:
- PostgreSQL
- Redis cache

Published Events:
- PriceChanged

Consumes:
- None initially

Interactions:
- Cart Service fetches live prices
- Catalog Service displays latest prices
- Order Service validates final order amount

---

## Cart Service
Persistent shopping cart service.

Responsibilities:
- Add/remove cart items
- Persist carts
- Validate stock before checkout
- Calculate totals using pricing service

Database:
- Redis
- Optional PostgreSQL persistence

Published Events:
- CartCreated
- CartUpdated
- CartCheckedOut

Consumes:
- PriceChanged
- StockUpdated

Interactions:
- Fetches prices from Pricing Service
- Validates stock with Inventory Service
- Sends checkout payload to Order Service

---

## Order Service
Core orchestration service for order lifecycle.

Responsibilities:
- Create and manage orders
- Coordinate inventory reservation
- Trigger payments
- Trigger shipping workflows
- Maintain order states

Database:
- PostgreSQL

Order Lifecycle:
- CREATED
- PENDING_PAYMENT
- PAID
- SHIPPED
- DELIVERED
- CANCELLED

Published Events:
- OrderCreated
- OrderPaid
- OrderCancelled
- OrderShipped

Consumes:
- PaymentSucceeded
- PaymentFailed
- ShipmentCreated
- ShipmentDelivered

Interactions:
- Reserves stock with Inventory Service
- Initiates payment through Payment Service
- Triggers shipping workflow
- Sends notifications

---

## Payment Service
Handles payment processing.

Responsibilities:
- Integrate with payment providers
- Handle payment webhooks
- Track transaction status
- Process refunds

Database:
- PostgreSQL

Published Events:
- PaymentSucceeded
- PaymentFailed
- RefundProcessed

Consumes:
- OrderCreated

Interactions:
- Updates Order Service with payment result
- Triggers payment notifications

External Integrations:
- Stripe
- Razorpay
- PayPal

---

## Shipping Service
Handles shipment creation and delivery tracking.

Responsibilities:
- Shipping rate calculations
- Label generation
- Courier integrations
- Shipment tracking

Database:
- PostgreSQL
- Optional MongoDB for courier payloads

Published Events:
- ShipmentCreated
- ShipmentDelivered

Consumes:
- OrderPaid

Interactions:
- Updates Order Service with shipment status
- Sends tracking information to Notification Service

---

## Review Service
Manages customer reviews and ratings.

Responsibilities:
- Store reviews and ratings
- Review moderation
- Rating aggregation

Database:
- PostgreSQL
- Elasticsearch

Published Events:
- ReviewCreated

Consumes:
- None initially

Interactions:
- Catalog Service updates average ratings
- Recommendation Service consumes review data

---

## Recommendation Service
Provides personalized recommendations.

Responsibilities:
- Related books
- Personalized suggestions
- Purchase pattern analysis

Database:
- Neo4j / Redis / MongoDB

Published Events:
- Optional recommendation update events

Consumes:
- OrderPaid
- ReviewCreated
- BookCreated

Interactions:
- Uses analytics and purchase history
- Provides APIs to frontend

---

## Notification Service
Handles all customer communication.

Responsibilities:
- Email notifications
- SMS notifications
- Push notifications
- Notification templates

Database:
- MongoDB

Published Events:
- NotificationSent

Consumes:
- UserCreated
- OrderCreated
- PaymentSucceeded
- PaymentFailed
- ShipmentCreated
- ShipmentDelivered

Interactions:
- Sends customer communications for all major workflows

---

## Analytics Service
Centralized reporting and analytics engine.

Responsibilities:
- Consume platform-wide events
- Generate dashboards and reports
- Sales and user analytics
- Inventory insights
- Operational metrics

Database:
- Kafka event streams
- ClickHouse / PostgreSQL / Elasticsearch

Consumes:
- All platform events

Interactions:
- Provides reporting APIs
- Supports recommendation engine
- Enables operational dashboards

---

# Event-Driven Architecture

The platform heavily uses Kafka for asynchronous communication.

Benefits:
- Loose coupling between services
- Independent scalability
- Improved resilience
- Event replay capability
- Real-time analytics

---

# Kafka Event Flows

## Inventory Flow

Inventory Service publishes:
- StockUpdated
- StockReserved
- StockReleased

Consumers:
- Catalog Service updates availability
- Cart Service validates quantities
- Order Service validates reservations
- Analytics Service tracks stock metrics

---

## Order Flow

Order Service publishes:
- OrderCreated
- OrderPaid
- OrderCancelled
- OrderShipped

Consumers:
- Payment Service initiates payments
- Inventory Service reserves/releases stock
- Shipping Service starts shipment workflow
- Notification Service sends customer updates
- Recommendation Service updates purchase graph
- Analytics Service tracks order metrics

---

## Payment Flow

Payment Service publishes:
- PaymentSucceeded
- PaymentFailed

Consumers:
- Order Service updates order state
- Notification Service sends receipts/failure alerts
- Analytics Service updates revenue metrics

---

## Shipping Flow

Shipping Service publishes:
- ShipmentCreated
- ShipmentDelivered

Consumers:
- Order Service updates order status
- Notification Service sends tracking updates
- Analytics Service measures delivery performance

---

## Review Flow

Review Service publishes:
- ReviewCreated

Consumers:
- Catalog Service updates ratings
- Recommendation Service improves recommendations
- Analytics Service tracks engagement

---

# Example End-to-End Order Flow

## Step 1 — User Authentication
- User logs in via Auth Service
- JWT token generated
- API Gateway validates token for subsequent requests

---

## Step 2 — Browse Catalog
- Client calls Catalog Service through API Gateway
- Catalog Service fetches:
    - Book metadata
    - Availability from inventory
    - Pricing information
    - Ratings

---

## Step 3 — Add to Cart
- User adds items to Cart Service
- Cart validates:
    - Stock via Inventory Service
    - Price via Pricing Service

---

## Step 4 — Checkout
- Cart publishes CartCheckedOut
- Order Service creates order
- Order Service publishes OrderCreated

---

## Step 5 — Inventory Reservation
- Inventory Service consumes OrderCreated
- Stock gets reserved
- StockReserved event published

---

## Step 6 — Payment Processing
- Payment Service consumes OrderCreated
- Payment processed through PSP
- PaymentSucceeded event published

---

## Step 7 — Order Finalization
- Order Service consumes PaymentSucceeded
- Order marked PAID
- OrderPaid event published

---

## Step 8 — Shipping
- Shipping Service consumes OrderPaid
- Shipment created
- ShipmentCreated event published

---

## Step 9 — Notifications
- Notification Service consumes:
    - OrderCreated
    - PaymentSucceeded
    - ShipmentCreated
- Sends emails/SMS to customer

---

## Step 10 — Analytics
- Analytics Service consumes all events silently
- Updates dashboards and reports

---

# Database Strategy

The system follows polyglot persistence.

| Service | Database |
|---|---|
| Auth Service | PostgreSQL |
| Catalog Service | MongoDB + Elasticsearch |
| Inventory Service | PostgreSQL |
| Pricing Service | PostgreSQL + Redis |
| Cart Service | Redis |
| Order Service | PostgreSQL |
| Payment Service | PostgreSQL |
| Shipping Service | PostgreSQL |
| Review Service | PostgreSQL + Elasticsearch |
| Recommendation Service | Neo4j / Redis |
| Notification Service | MongoDB |
| Analytics Service | ClickHouse / Elasticsearch |

---

# Technology Stack

Backend:
- Spring Boot
- Spring Cloud
- Spring Security
- Spring Data JPA
- Spring Kafka

Infrastructure:
- Kafka
- Redis
- PostgreSQL
- MongoDB
- Elasticsearch
- Docker
- Kubernetes

Observability:
- Prometheus
- Grafana
- ELK Stack
- OpenTelemetry

---

# Design Principles

The project is designed around:
- Microservices architecture
- Domain-driven design
- Event-driven communication
- Independent deployability
- Loose coupling
- High scalability
- Fault tolerance
- Polyglot persistence

---

# Future Enhancements

Potential future improvements:
- CQRS
- Event sourcing
- Saga orchestration
- Distributed tracing
- AI-powered recommendations
- Real-time inventory sync
- Multi-region deployment
- Kubernetes autoscaling
- Advanced caching strategies

---

# Project Goal

This project is intended to simulate how a real enterprise-grade e-commerce platform is architected using modern distributed systems patterns.

The focus is not only on APIs, but also on:
- Service communication
- Event-driven workflows
- Scalability
- Resilience
- Distributed transactions
- Data consistency
- Production architecture patterns

