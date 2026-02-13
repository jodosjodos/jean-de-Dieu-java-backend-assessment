# TeleTrack360 - Incident & Reporting System

## Project Overview

TeleTrack360 is a modernized microservices-based incident tracking and reporting system designed for Telekom teams. This project refactors a legacy monolithic Java application into a scalable, observable, and secure microservices architecture.

### Key Features

- **Microservices Architecture**: 4 core services (User, Incident, Notification, Reporting)
- **Event-Driven Communication**: Apache Kafka for inter-service messaging
- **Secure Authentication**: OAuth2/JWT-based authentication and authorization
- **Comprehensive Observability**: ELK Stack, Prometheus, and Grafana
- **API Documentation**: Swagger/OpenAPI 3 for all endpoints
- **Database Per Service**: PostgreSQL with Liquibase migrations
- **Containerized Deployment**: Docker and Docker Compose

## Technology Stack

| Category | Technology |
|----------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.2, Spring Cloud |
| **Security** | Spring Security, OAuth2, JWT |
| **Database** | PostgreSQL 15, JPA/Hibernate, Liquibase |
| **Messaging** | Apache Kafka 3.6.1 |
| **API Gateway** | Spring Cloud Gateway |
| **Config Management** | Spring Cloud Config Server |
| **Documentation** | SpringDoc OpenAPI 3 |
| **Testing** | JUnit 5, Mockito, Testcontainers, JaCoCo |
| **Observability** | Prometheus, Grafana, ELK Stack |
| **Build Tool** | Maven 3.9+ |
| **Containerization** | Docker, Docker Compose |

## Architecture

### System Architecture Diagram

```
┌─────────────────┐
│   Client Apps   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  API Gateway    │
│  (Port: 8080)   │
└────────┬────────┘
         │
    ┌────┴────┬────────────┬──────────────┐
    │         │            │              │
    ▼         ▼            ▼              ▼
┌────────┐ ┌──────┐ ┌─────────┐ ┌──────────┐
│ User   │ │Incid-│ │Notifica-│ │Reporting │
│Service │ │ent   │ │tion     │ │Service   │
│:8081   │ │:8082 │ │:8083    │ │:8084     │
└───┬────┘ └──┬───┘ └────┬────┘ └────┬─────┘
    │         │          │           │
    ▼         ▼          ▼           ▼
┌────────┐ ┌──────┐  ┌──────┐   ┌──────┐
│UserDB  │ │Incid-│  │Notif-│   │Report│
│        │ │entDB │  │DB    │   │DB    │
└────────┘ └──┬───┘  └──────┘   └──────┘
              │
              ▼
         ┌─────────┐
         │  Kafka  │
         │  :9092  │
         └────┬────┘
              │
    ┌─────────┴──────────┐
    │                    │
    ▼                    ▼
┌──────────┐      ┌──────────┐
│Prometheus│      │   ELK    │
│Grafana   │      │  Stack   │
└──────────┘      └──────────┘
```

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **Git**

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd jean-de-Dieu-java-backend-assessment
```

### 2. Build All Services

```bash
mvn clean install
```

### 3. Start Infrastructure Services

```bash
docker-compose up -d postgres-user postgres-incident postgres-notification kafka zookeeper
```

Wait for services to be healthy (check with `docker-compose ps`).

### 4. Run Services Locally

#### Terminal 1 - User Service
```bash
cd services/user-service
mvn spring-boot:run
```

#### Terminal 2 - Incident Service
```bash
cd services/incident-service
mvn spring-boot:run
```

### 5. Start Observability Stack

```bash
docker-compose up -d prometheus grafana elasticsearch logstash kibana
```

## Service Endpoints

| Service | Port | Swagger UI | Actuator |
|---------|------|------------|----------|
| User Service | 8081 | http://localhost:8081/swagger-ui.html | http://localhost:8081/actuator |
| Incident Service | 8082 | http://localhost:8082/swagger-ui.html | http://localhost:8082/actuator |
| Notification Service | 8083 | http://localhost:8083/swagger-ui.html | http://localhost:8083/actuator |
| Reporting Service | 8084 | http://localhost:8084/swagger-ui.html | http://localhost:8084/actuator |

## Observability Dashboards

- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Kibana**: http://localhost:5601

## API Usage Examples

### 1. Register a New User

```bash
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test@123",
    "firstName": "Test",
    "lastName": "User",
    "role": "OPERATOR"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123"
  }'
```

Save the `accessToken` from the response.

### 3. Create an Incident

```bash
curl -X POST http://localhost:8082/api/v1/incidents \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-access-token>" \
  -d '{
    "title": "Server Down",
    "description": "Production server is not responding",
    "priority": "HIGH"
  }'
```

### 4. List All Incidents

```bash
curl -X GET "http://localhost:8082/api/v1/incidents?page=0&size=20" \
  -H "Authorization: Bearer <your-access-token>"
```

## Default Users

The system comes with pre-seeded users for testing:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | Admin@123 | ADMIN | admin@teletrack360.com |
| operator | Operator@123 | OPERATOR | operator@teletrack360.com |
| support | Support@123 | SUPPORT | support@teletrack360.com |

## Project Structure

```
teletrack360/
├── services/
│   ├── user-service/           # Authentication & user management
│   ├── incident-service/        # Incident CRUD & Kafka producer
│   ├── notification-service/    # Kafka consumer & notifications
│   └── reporting-service/       # Analytics & reporting
├── gateway/
│   └── api-gateway/             # API routing & security
├── config/
│   └── config-service/          # Centralized configuration
├── shared/
│   └── common-utils/            # Shared DTOs & utilities
├── deployment/
│   ├── docker/                  # Docker configurations
│   └── k8s/                     # Kubernetes manifests
├── docs/
│   ├── HLD.md                   # High-Level Design
│   ├── LLD.md                   # Low-Level Design
│   └── DevReflection.md         # Development reflection
├── docker-compose.yml
└── pom.xml
```

## Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Generate Coverage Report

```bash
mvn clean test jacoco:report
```

Coverage reports are generated in `target/site/jacoco/index.html` for each service.

## Kafka Topics

| Topic | Producer | Consumer | Purpose |
|-------|----------|----------|---------|
| `incident-created` | Incident Service | Notification Service | New incident notifications |
| `incident-updated` | Incident Service | Notification Service | Update notifications |
| `incident-assigned` | Incident Service | Notification Service | Assignment notifications |
| `incident-resolved` | Incident Service | Notification, Reporting | Resolution notifications |

## Database Migrations

Database schemas are managed using Liquibase. Migrations run automatically on application startup.

To manually run migrations:

```bash
mvn liquibase:update
```

## Troubleshooting

### Services Won't Start

1. Check if PostgreSQL is running: `docker-compose ps`
2. Verify database connectivity: `docker-compose logs postgres-user`
3. Check application logs: `docker-compose logs user-service`

### Kafka Connection Issues

1. Ensure Kafka and Zookeeper are running:
   ```bash
   docker-compose ps kafka zookeeper
   ```
2. Check Kafka logs:
   ```bash
   docker-compose logs kafka
   ```

### Port Conflicts

If you encounter port conflicts, modify the port mappings in `docker-compose.yml`.

## CI/CD Pipeline

The project includes a GitHub Actions workflow (`.github/workflows/ci-cd.yml`) that:

1. Builds all services
2. Runs tests and generates coverage reports
3. Builds Docker images
4. Pushes images to container registry

## Performance Considerations

- **Connection Pooling**: Hikari CP configured for optimal database connections
- **Kafka Batching**: Producer configured with acks=all and retries=3
- **JVM Tuning**: Recommended settings for production:
  ```bash
  -Xms512m -Xmx2048m -XX:+UseG1GC
  ```

## Security

- **JWT Tokens**: Access tokens expire in 1 hour, refresh tokens in 7 days
- **Password Hashing**: BCrypt with strength 10
- **HTTPS**: Recommended for production deployments
- **API Rate Limiting**: Configured at API Gateway level

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m "Add your feature"`
3. Push to branch: `git push origin feature/your-feature`
4. Create a Pull Request

## License

This project is part of a technical assessment for TeleTrack360.

## Contact

For questions or issues, please contact: support@teletrack360.com

---

**Built with ❤️ using Spring Boot and modern microservices architecture**
