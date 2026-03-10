# Best Fly Backend

REST API for authentication, flight search, and management of users, favorite flights, and search history.

Link here: https://bestflyapi.duckdns.org/swagger-ui/index.html

## Technologies
- Java 25
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Redis
- Springdoc OpenAPI (Swagger)

## How to Run

### 1. Environment Variables
Set the following environment variables before starting the application:

- `DB_PASSWORD`: PostgreSQL database password
- `JWT_SECRET`: secret for JWT token signing
- `DUFFEL_TOKEN`: Duffel API token for flight search

### 2. Start Local Dependencies
```bash
docker compose up -d
```

### 3. Start the Application
```bash
./mvnw spring-boot:run
```
On Windows (PowerShell):
```powershell
.\mvnw.cmd spring-boot:run
```

## Swagger Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication
Protected endpoints use JWT in the header:

```http
Authorization: Bearer <token>
```

## Endpoints

### Authentication (public)

#### `POST /auth/register`
Creates a user and returns a token.

Request body:
```json
{
  "email": "user@email.com",
  "password": "123456",
  "firstName": "Pedro",
  "lastName": "Pareschi"
}
```

Responses:
- `201 Created`
- `400 Bad Request`

#### `POST /auth/login`
Authenticates a user and returns a token.

Request body:
```json
{
  "email": "user@email.com",
  "password": "123456"
}
```

Responses:
- `200 OK`
- `401 Unauthorized`

### Places (public)

#### `GET /places?query={text}`
Searches for airports/locations.

Responses:
- `200 OK`
- `400 Bad Request`

### Flights (public)

#### `GET /flights`
Searches for flight offers.

Query params:
- `origin` (required)
- `destination` (required)
- `departureDate` (required, `YYYY-MM-DD`)
- `departureTime` (optional, `HH:mm`)
- `numberOfAdults` (required)
- `numberOfChildren` (optional, default `0`)
- `returnDate` (optional, `YYYY-MM-DD`)
- `returnTime` (optional, `HH:mm`)
- `limit` (optional, default `20`)
- `after` (optional, pagination cursor)

Responses:
- `200 OK`
- `400 Bad Request`

### Users (protected)

#### `GET /users`
Lists users.

Responses:
- `200 OK`
- `401 Unauthorized`

#### `GET /users/{id}`
Retrieves a user by ID.

Responses:
- `200 OK`
- `401 Unauthorized`
- `404 Not Found`

#### `PUT /users/{id}`
Updates a user by ID.

Request body:
```json
{
  "email": "new@email.com",
  "password": "newpassword",
  "firstName": "Pedro",
  "lastName": "Pareschi",
  "city": "Sao Paulo"
}
```

Responses:
- `200 OK`
- `401 Unauthorized`
- `404 Not Found`

#### `DELETE /users/{id}`
Deletes a user by ID.

Responses:
- `204 No Content`
- `401 Unauthorized`
- `404 Not Found`

### Favorite Flights (protected)

#### `GET /favorite-flights`
Lists favorite flights.

Responses:
- `200 OK`
- `401 Unauthorized`

#### `GET /favorite-flights/{id}`
Retrieves a favorite flight by ID.

Responses:
- `200 OK`
- `401 Unauthorized`
- `404 Not Found`

#### `POST /favorite-flights`
Creates a favorite flight.

Request body:
```json
{
  "offer": {}
}
```

Responses:
- `201 Created`
- `401 Unauthorized`
- `404 Not Found`

#### `PUT /favorite-flights/{id}`
Updates a favorite flight.

Request body:
```json
{
  "offer": {}
}
```

Responses:
- `200 OK`
- `401 Unauthorized`
- `404 Not Found`

#### `DELETE /favorite-flights/{id}`
Deletes a favorite flight.

Responses:
- `204 No Content`
- `401 Unauthorized`
- `404 Not Found`

### Search History (protected)

#### `GET /search-history`
Lists search history.

Responses:
- `200 OK`
- `401 Unauthorized`

#### `GET /search-history/{id}`
Retrieves a search history item by ID.

Responses:
- `200 OK`
- `401 Unauthorized`
- `404 Not Found`

#### `POST /search-history`
Creates a search history item.

Request body:
```json
{
  "offerRequestId": "offer_123",
  "originLocation": "Sao Paulo",
  "destinationLocation": "New York",
  "departureDate": "2026-04-10T09:00:00",
  "numberOfAdults": 1,
  "numberOfChildren": 0,
  "returnDate": "2026-04-20T20:00:00"
}
```

Responses:
- `201 Created`
- `401 Unauthorized`
- `400 Bad Request`

#### `DELETE /search-history/{id}`
Deletes a search history item.

Responses:
- `204 No Content`
- `401 Unauthorized`
- `404 Not Found`
