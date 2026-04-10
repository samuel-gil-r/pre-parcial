# ECI-SportLife API

API REST para tienda deportiva virtual desarrollada con Spring Boot 3.x, PostgreSQL y MongoDB.

---

## Descripción del proyecto

ECI-SportLife permite a los usuarios explorar un catálogo de productos deportivos, gestionar un carrito de compras persistido en MongoDB y realizar el proceso de pago generando una orden de compra en PostgreSQL. La autenticación se basa en JWT con roles USER y ADMIN.

---

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Spring Boot 3.2 | Framework base |
| Maven | Gestión de dependencias |
| Spring Security + JWT (jjwt 0.11.5) | Autenticación y autorización |
| Spring Data JPA + PostgreSQL | Persistencia relacional (usuarios, productos, órdenes) |
| Spring Data MongoDB | Persistencia no relacional (carrito) |
| Lombok | Reducción de boilerplate |
| MapStruct | Mapeo entre capas |
| SpringDoc OpenAPI (Swagger) | Documentación interactiva |
| JUnit 5 + Mockito | Pruebas unitarias e integración |
| JaCoCo | Cobertura de código |
| SonarQube | Análisis estático de calidad |
| SLF4J + Logback | Logging |

---

## Matriz de trazabilidad

| ID | Funcionalidad | Endpoint | Rol | Autenticación |
|---|---|---|---|---|
| F1 | Registro de usuario | POST /api/auth/register | Público | No |
| F2 | Login de usuario | POST /api/auth/login | Público | No |
| F3 | Listar productos activos | GET /api/products | Público | No |
| F4 | Detalle de producto | GET /api/products/{id} | Público | No |
| F5 | Agregar ítem al carrito | POST /api/cart/items | USER | JWT |
| F6 | Ver carrito | GET /api/cart | USER | JWT |
| F7 | Checkout / Pago | POST /api/orders/checkout | USER | JWT |

---

## Endpoints

### F1 — POST /api/auth/register

Registra un nuevo usuario en el sistema.

- **Verbo:** POST — No idempotente (cada llamada puede crear un recurso distinto o fallar si el email ya existe)
- **Autenticación:** No requerida

**Request Body:**
```json
{
  "name": "Juan Pérez",       // String, obligatorio
  "email": "juan@email.com",  // String (email válido), obligatorio
  "password": "securePass1"   // String (min 8 chars), obligatorio
}
```

**Response 201 Created:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Pérez",
  "email": "juan@email.com",
  "role": "USER",
  "createdAt": "2026-04-10T12:00:00"
}
```

**Validaciones:** email formato válido y único, password ≥ 8 caracteres, name no vacío

**Códigos HTTP:**
- 201 Created — Registro exitoso
- 400 Bad Request — Validación de campo fallida
- 409 Conflict — Email ya registrado

---

### F2 — POST /api/auth/login

Autentica un usuario y devuelve un token JWT.

- **Verbo:** POST — No idempotente (genera un nuevo token en cada llamada)
- **Autenticación:** No requerida

**Request Body:**
```json
{
  "email": "juan@email.com",  // String (email válido), obligatorio
  "password": "securePass1"   // String, obligatorio
}
```

**Response 200 OK:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}
```

**Códigos HTTP:**
- 200 OK — Login exitoso
- 400 Bad Request — Campos inválidos
- 404 Not Found — Credenciales incorrectas

---

### F3 — GET /api/products

Lista todos los productos activos con filtros opcionales.

- **Verbo:** GET — Idempotente: múltiples llamadas idénticas devuelven el mismo resultado sin efectos secundarios
- **Autenticación:** No requerida

**Query Params:** `category` (opcional, String), `name` (opcional, String, búsqueda parcial)

**Response 200 OK:**
```json
[
  {
    "id": "uuid",
    "name": "Zapatillas Running Pro",
    "description": "Alta amortiguación",
    "category": "calzado",
    "price": 120.00,
    "stock": 15,
    "images": ["https://..."],
    "status": "ACTIVE",
    "createdAt": "2026-04-10T10:00:00"
  }
]
```

**Códigos HTTP:**
- 200 OK — Listado exitoso (puede ser lista vacía)
- 500 Internal Server Error — Error del servidor

---

### F4 — GET /api/products/{id}

Devuelve el detalle de un producto por su UUID.

- **Verbo:** GET — Idempotente: leer un recurso no lo modifica
- **Autenticación:** No requerida

**Path Variable:** `id` (UUID, obligatorio)

**Response 200 OK:**
```json
{
  "id": "550e8400-...",
  "name": "Balón Fútbol FIFA",
  "description": "Reglamentario",
  "category": "balones",
  "price": 89.99,
  "stock": 5,
  "images": [],
  "status": "ACTIVE",
  "createdAt": "2026-04-01T08:00:00"
}
```

**Códigos HTTP:**
- 200 OK — Producto encontrado
- 404 Not Found — Producto no existe

---

### F5 — POST /api/cart/items

Agrega un producto al carrito del usuario autenticado.

- **Verbo:** POST — No idempotente (llamadas repetidas suman la cantidad)
- **Autenticación:** JWT requerido

**Request Body:**
```json
{
  "productId": "uuid",  // UUID, obligatorio
  "quantity": 2          // Integer >= 1, obligatorio
}
```

**Response 201 Created:**
```json
{
  "id": "mongo-id",
  "userId": "uuid",
  "items": [
    {
      "productId": "uuid",
      "productName": "Zapatillas Running Pro",
      "price": 120.00,
      "quantity": 2,
      "subtotal": 240.00
    }
  ],
  "total": 240.00,
  "updatedAt": "2026-04-10T15:30:00"
}
```

**Validaciones:** producto existe y está ACTIVE, stock suficiente, quantity ≥ 1

**Códigos HTTP:**
- 201 Created — Ítem agregado
- 400 Bad Request — Stock insuficiente o cantidad inválida
- 401 Unauthorized — Sin JWT
- 404 Not Found — Producto no existe

---

### F6 — GET /api/cart

Devuelve el resumen del carrito del usuario autenticado.

- **Verbo:** GET — Idempotente
- **Autenticación:** JWT requerido

**Response 200 OK:** (misma estructura que F5)

**Códigos HTTP:**
- 200 OK — Carrito devuelto (puede estar vacío)
- 401 Unauthorized — Sin JWT

---

### F7 — POST /api/orders/checkout

Procesa el pago del carrito y genera una orden de compra.

- **Verbo:** POST — No idempotente (crea una orden y vacía el carrito)
- **Autenticación:** JWT requerido

**Request Body:** Ninguno (usa el carrito del usuario autenticado)

**Response 201 Created:**
```json
{
  "id": "uuid",
  "userId": "uuid",
  "items": [
    {
      "productId": "uuid",
      "productName": "Zapatillas Running Pro",
      "quantity": 2,
      "subtotal": 240.00
    }
  ],
  "total": 240.00,
  "status": "PAID",
  "transactionId": "txn-uuid",
  "createdAt": "2026-04-10T15:35:00"
}
```

**Códigos HTTP:**
- 201 Created — Orden creada (PAID o REJECTED)
- 400 Bad Request — Carrito vacío o stock insuficiente
- 401 Unauthorized — Sin JWT
- 402 Payment Required — Pago rechazado por pasarela

---

## Diagrama de componentes general (ASCII)

```
┌─────────────────────────────────────────────────────────┐
│                     Cliente HTTP                         │
└───────────────────┬────────────────────────────────────┘
                    │ HTTP/HTTPS
┌───────────────────▼────────────────────────────────────┐
│                  Spring Boot App                         │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │  Controller │→ │ Core/Service │→ │  Persistence  │  │
│  │  (REST API) │  │  (Dominio)   │  │  (JPA/Mongo)  │  │
│  └─────────────┘  └──────────────┘  └───────┬───────┘  │
│                                              │           │
└──────────────────────────────────────────────┼──────────┘
                                               │
                    ┌──────────────────────────┼──────────┐
                    │         ▼                │   ▼      │
                    │   PostgreSQL         MongoDB         │
                    │ (users, products,   (carts)          │
                    │   orders)                            │
                    └──────────────────────────────────────┘
```

## Diagrama de componentes específico (ASCII)

```
controller/
├── AuthController ──────────────── AuthService (interface)
│                                        └── AuthServiceImpl
├── ProductController ───────────── ProductService
│                                        └── ProductServiceImpl
├── CartController ──────────────── CartService
│                                        └── CartServiceImpl
└── OrderController ─────────────── OrderService
                                         └── OrderServiceImpl
                                              └── PaymentStrategy (interface)
                                                   └── CreditCardPaymentStrategy

Validators ────────── UserValidator, ProductValidator, CartValidator, OrderValidator
Utils      ────────── JwtUtil, PasswordUtil
Config     ────────── SecurityConfig, JwtFilter, OpenApiConfig
```

---

## Patrones de diseño implementados

| Patrón | Ubicación | Por qué |
|---|---|---|
| **Repository** | `persistence/repositories/` | Abstrae el acceso a datos; permite cambiar la implementación sin tocar la lógica de negocio |
| **DTO** | `controller/dtos/` | Desacopla la API pública de las entidades internas; evita exponer campos sensibles como password |
| **Strategy** | `core/services/PaymentStrategy` | Permite agregar nuevos métodos de pago (PayPal, PSE) sin modificar `OrderServiceImpl` |
| **Singleton** | `JwtUtil` (Spring `@Component`) | Spring garantiza una única instancia compartida; centraliza la lógica de tokens |
| **Mapper (MapStruct)** | `persistence/mappers/`, `controller/mappers/` | Convierte entre capas de forma segura en tiempo de compilación, sin reflexión en runtime |

---

## Modelo relacional (PostgreSQL)

**Tabla `users`**
| Columna | Tipo | Restricción |
|---|---|---|
| id | UUID | PK |
| name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| role | VARCHAR(20) | NOT NULL (USER/ADMIN) |
| created_at | TIMESTAMP | NOT NULL |

**Tabla `products`**
| Columna | Tipo | Restricción |
|---|---|---|
| id | UUID | PK |
| name | VARCHAR(255) | NOT NULL |
| description | TEXT | |
| category | VARCHAR(100) | NOT NULL |
| price | DECIMAL(12,2) | NOT NULL |
| stock | INTEGER | NOT NULL |
| status | VARCHAR(20) | NOT NULL (ACTIVE/INACTIVE) |
| created_at | TIMESTAMP | NOT NULL |

**Tabla `product_images`** (colección de strings de ProductEntity)
| Columna | Tipo |
|---|---|
| product_id | UUID (FK → products.id) |
| image_url | VARCHAR(500) |

**Tabla `orders`**
| Columna | Tipo | Restricción |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | NOT NULL |
| total | DECIMAL(12,2) | NOT NULL |
| status | VARCHAR(20) | NOT NULL (PENDING/PAID/REJECTED) |
| transaction_id | VARCHAR(255) | |
| created_at | TIMESTAMP | NOT NULL |

**Tabla `order_items`** (embebida en orders)
| Columna | Tipo |
|---|---|
| order_id | UUID (FK → orders.id) |
| product_id | UUID |
| product_name | VARCHAR(255) |
| quantity | INTEGER |
| subtotal | DECIMAL(12,2) |

---

## Modelo no relacional (MongoDB)

**Colección `carts`**
```json
{
  "_id": "ObjectId",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [
    {
      "productId": "uuid",
      "productName": "Zapatillas Running Pro",
      "price": 120.00,
      "quantity": 2,
      "subtotal": 240.00
    }
  ],
  "total": 240.00,
  "createdAt": "2026-04-10T10:00:00",
  "updatedAt": "2026-04-10T15:30:00"
}
```

Se usa MongoDB para el carrito porque:
- El esquema es flexible (ítems pueden variar)
- Las operaciones de lectura/escritura son frecuentes y requieren baja latencia
- No requiere integridad referencial estricta; se valida a nivel de servicio

---

## Seguridad

### JWT (JSON Web Token)
- Generado en login, expiración de 24 horas
- Firmado con HMAC-SHA256 usando clave secreta configurable
- **Ventajas:** stateless (no requiere sesión en servidor), escalable horizontalmente, portable entre microservicios

### BCrypt
- Contraseñas nunca almacenadas en texto plano
- Factor de trabajo adaptable (BCryptPasswordEncoder)

### CORS
CORS (Cross-Origin Resource Sharing) es necesario porque el frontend (React, Angular) se sirve desde un origen diferente al backend. Sin configurar CORS, el navegador bloqueará las peticiones por política de mismo origen. Se configura en `SecurityConfig.corsConfigurationSource()`.

### TLS/SSL
En producción, el tráfico debe estar cifrado con TLS (Transport Layer Security). Se configuraria en el servidor de aplicaciones (Nginx/Load Balancer) con certificados SSL, garantizando que el JWT viaje cifrado y no pueda ser interceptado (ataque MITM).

### Roles y permisos

| Endpoint | Público | USER | ADMIN |
|---|---|---|---|
| POST /api/auth/register | ✓ | | |
| POST /api/auth/login | ✓ | | |
| GET /api/products | ✓ | | |
| GET /api/products/{id} | ✓ | | |
| POST /api/cart/items | | ✓ | ✓ |
| GET /api/cart | | ✓ | ✓ |
| POST /api/orders/checkout | | ✓ | ✓ |

---

## Instrucciones para correr el proyecto localmente

### Prerrequisitos
- Java 17+
- Maven 3.8+
- Docker (recomendado para bases de datos)

### 1. Levantar PostgreSQL y MongoDB con Docker

```bash
docker run -d --name sportlife-postgres \
  -e POSTGRES_DB=sportlife \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:15

docker run -d --name sportlife-mongo \
  -p 27017:27017 mongo:6
```

### 2. Configurar variables de entorno

```bash
export DB_URL=jdbc:postgresql://localhost:5432/sportlife
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export MONGO_URI=mongodb://localhost:27017/sportlife
export JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
export JWT_EXPIRATION=86400000
```

### 3. Compilar y ejecutar

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

### 4. Acceder a la documentación

Abrir en el navegador: http://localhost:8080/swagger-ui.html

### 5. Ejecutar pruebas

```bash
mvn test
```

### 6. Ver reporte de cobertura JaCoCo

```bash
open target/site/jacoco/index.html
```

---

## Variables de entorno necesarias

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/sportlife` |
| `DB_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `postgres` |
| `MONGO_URI` | URI de conexión MongoDB | `mongodb://localhost:27017/sportlife` |
| `JWT_SECRET` | Clave secreta Base64 para firmar JWT | *(ver application.yaml)* |
| `JWT_EXPIRATION` | Tiempo de expiración del JWT en ms | `86400000` (24h) |
| `SONAR_TOKEN` | Token de autenticación SonarQube | *(requerido en CI)* |
| `SONAR_HOST_URL` | URL del servidor SonarQube | `http://localhost:9000` |
