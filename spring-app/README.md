# QFieldCloud Spring Boot Replica

This is a Spring Boot 3.x replication of the QFieldCloud Django backend, focusing on the `qfieldcloud/` and `core/` modules.

## Architecture Mapping

| Django Concept | Spring Boot Equivalent |
|---------------|----------------------|
| Django Model (`models.Model`) | JPA Entity (`@Entity`) |
| Django View (`APIView`, `ViewSet`) | `@RestController` |
| Django Serializer | DTO + `fromEntity()` factory methods |
| Django URLConf (`urls.py`) | `@RequestMapping` on controllers |
| Django Permission classes | Custom `QFieldCloudException` + validation |
| Django Exception Handler | `@RestControllerAdvice` (`GlobalExceptionHandler`) |
| Django Settings (`settings.py`) | `application.yml` + `@ConfigurationProperties` |
| Django Middleware | Spring Filters / Interceptors |
| Django Authentication (Token) | JWT + `JwtAuthenticationFilter` |
| Django Admin | Spring Boot Actuator |

## Project Structure

```
## Key Design Decisions

### User Inheritance
Django uses multi-table inheritance for `User -> Person/Organization/Team`. Spring Boot uses **SINGLE_TABLE** inheritance with a discriminator column (`user_type`) for simplicity and performance.

### Authentication
- Django uses DRF `TokenAuthentication`
- Spring Boot uses **JWT** (JSON Web Tokens) with `JwtAuthenticationFilter`
- Token header format: `Token <jwt>` (matching Django's `Token` prefix)

### Error Handling
Django's `QFieldCloudError` with `status_code` and `code` maps to Spring's `QFieldCloudException` hierarchy, handled globally by `GlobalExceptionHandler` (`@RestControllerAdvice`).

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Authenticate and get JWT token |
| `/api/v1/auth/user` | GET | Get current user info |
| `/api/v1/auth/logout` | POST | Logout (client-side token discard) |
| `/api/v1/users` | GET | List users |
| `/api/v1/users/{username}` | GET | Get user details |
| `/api/v1/projects` | GET | List projects |
| `/api/v1/projects/public` | GET | List public projects |
| `/api/v1/projects/{id}` | GET | Get project details |
| `/api/v1/projects` | POST | Create project |
| `/api/v1/jobs` | GET | List jobs |
| `/api/v1/jobs/{jobId}` | GET | Get job details |
| `/api/v1/jobs` | POST | Create job |
| `/api/v1/deltas?projectId=` | GET | List deltas for project |
| `/api/v1/deltas/{deltaId}` | GET | Get delta details |
| `/api/v1/members/{organization}` | GET | List organization members |
| `/api/v1/collaborators/{projectId}` | GET | List project collaborators |

## Build & Run

### Prerequisites
- Java 17+ (Java 21 tested)
- Maven 3.8+
- PostgreSQL database

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/spring-app-1.0.0.jar
```

### With Docker
```bash
docker build -t qfieldcloud-spring .
docker run -p 8080:8080 \
  -e POSTGRES_URL=jdbc:postgresql://db:5432/qfieldcloud \
  -e POSTGRES_USER=qfieldcloud \
  -e POSTGRES_PASSWORD=qfieldcloud \
  qfieldcloud-spring
```

### Configuration
All configuration is in `application.yml` and can be overridden via environment variables:

| Property | Env Var | Default |
|----------|---------|---------|
| `spring.datasource.url` | `POSTGRES_URL` | `jdbc:postgresql://localhost:5432/qfieldcloud` |
| `spring.datasource.username` | `POSTGRES_USER` | `qfieldcloud` |
| `spring.datasource.password` | `POSTGRES_PASSWORD` | `qfieldcloud` |
| `qfieldcloud.jwt.secret` | `QFIELDCLOUD_JWT_SECRET` | base64-encoded secret |
| `qfieldcloud.environment` | `ENVIRONMENT` | `development` |
| `server.port` | `SERVER_PORT` | `8080` |

## API Documentation

Swagger UI is available at:
- `http://localhost:8080/api/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/api/v3/api-docs`

## Limitations / TODO

The following Django features are not yet implemented:
- File storage / filestorage module
- Subscription module
- Notifications module
- SSO / allauth integration
- QGIS worker / job processing
- Audit logging
- Django Admin UI
- Project QGIS project file handling
- Delta apply processing
- Package job processing

## License

MIT License (same as QFieldCloud)
spring-app/
├── pom.xml
├── Dockerfile
├── README.md
├── application.yml
└── src/
    └── main/
        └── java/
            └── com/qfieldcloud/
                ├── core/
                │   ├── QFieldCloudApplication.java
                │   ├── config/AppProperties.java
                │   ├── application/controller/
                │   ├── application/dto/
                │   ├── application/exception/
                │   ├── application/security/
                │   ├── domain/model/
                │   └── domain/repository/
                ├── project/
                │   ├── application/controller/
                │   ├── application/dto/
                │   ├── domain/model/
                │   └── domain/repository/
                └── authentication/
                    ├── application/controller/
                    ├── application/dto/
                    └── domain/repository/
```