# GFT Formación — API REST de Gestión de Usuarios

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9.14-blue?logo=apachemaven)
![JUnit 5](https://img.shields.io/badge/Tests-JUnit%205%20%2B%20Mockito-green?logo=junit5)
![JaCoCo](https://img.shields.io/badge/Cobertura-100%25-success)
![License](https://img.shields.io/badge/Licencia-Educativa-lightgrey)

API REST completa desarrollada con Spring Boot como parte del **GFT Junior Training Programme** (back-end track). Demuestra buenas prácticas de desarrollo empresarial: Clean Code, SOLID, JPA con relaciones ManyToMany, DTOs, AOP, perfiles de configuración, cobertura de tests al 100% y CI/CD.

---

## Características

- CRUD completo de usuarios con paginación y búsqueda por nombre
- Relación **ManyToMany** entre `User` y `Allergy` persistida en H2 vía JPA
- DTOs de respuesta (`UserDTO`, `AllergyDTO`) para evitar ciclos de serialización
- Query personalizada con `@Query` + `@Modifying` + `@Transactional` para `PATCH` de nombre
- `DataInitializer` que precarga 30 usuarios y 5 alergías al arrancar
- Múltiples perfiles de configuración (`local`, `dev`, `prod`)
- Validación de datos con Jakarta Bean Validation
- Manejo centralizado de excepciones con `@RestControllerAdvice` y `ErrorResponse`
- Trazas de entrada/salida de métodos mediante AOP (`LoggingAspect`)
- Propiedades sensibles gestionadas con variables de entorno
- Cobertura de tests del **100%** (JUnit 5 + Mockito + JaCoCo)
- Tests unitarios, de integración completa (`FullIntegrationTest`) y de aspecto AOP
- CI con GitHub Actions (`ci.yml` + `sonarcloud.yml`)

---

## Tech Stack

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 4.0.4 | Framework base |
| Spring Data JPA | Persistencia y repositorios |
| H2 Database | Base de datos en memoria |
| Jakarta Bean Validation | Validación de entrada |
| Spring AOP / AspectJ | Logging transversal |
| Lombok | Reducción de boilerplate |
| Apache Commons Lang3 | Utilidades de strings |
| JUnit 5 + Mockito | Tests unitarios e integración |
| JaCoCo | Cobertura de código |
| Logback | Logging a consola y fichero |
| Maven 3.9.14 | Gestión de dependencias y build |

---

## Estructura del Proyecto

```
src/
├── main/java/com/exampleinyection/clase2parte2/
│   ├── GFTFormacion.java                    # Clase principal @SpringBootApplication
│   ├── aspect/
│   │   └── LoggingAspect.java               # AOP: traza entrada/salida/tiempo de métodos
│   ├── config/
│   │   ├── AppConfig.java                   # @ConfigurationProperties - perfiles YAML
│   │   ├── DataInitializer.java             # CommandLineRunner: precarga 30 users + 5 alergias
│   │   └── SpringContextHelper.java         # Helper para acceso a beans
│   ├── controller/
│   │   └── UserController.java              # Endpoints REST
│   ├── dto/
│   │   ├── AllergyDTO.java                  # Record: id, name, severity
│   │   ├── UserDTO.java                     # Record: id, name, age, List<AllergyDTO>
│   │   └── UserRequest.java                 # Record: DTO de entrada (name, age, allergies)
│   ├── exception/
│   │   ├── ErrorResponse.java               # Payload de error estructurado
│   │   ├── GlobalExceptionHandler.java      # @RestControllerAdvice centralizado
│   │   ├── InvalidUserException.java        # → HTTP 400
│   │   └── UserNotFoundException.java       # → HTTP 404
│   ├── model/
│   │   ├── Allergy.java                     # @Entity con @ManyToMany(mappedBy) + @JsonIgnore
│   │   └── User.java                        # @Entity con @ManyToMany + @JoinTable user_allergies
│   ├── repository/
│   │   ├── AllergyRepository.java           # JpaRepository<Allergy, Long>
│   │   └── UserRepository.java              # + @Query JPQL, @Modifying, findByNameContaining
│   └── service/
│       └── UserService.java                 # Lógica de negocio, mapeo anti-ciclos, defaults
└── test/java/com/exampleinyection/clase2parte2/
    ├── GFTFormacionTests.java
    ├── aspect/
    │   └── LoggingAspectTest.java           # Mockea ProceedingJoinPoint, cubre excepción
    ├── config/
    │   ├── AppConfigIntegrationTest.java
    │   ├── AppConfigTest.java
    │   ├── AppConfigTestWithPropertySource.java
    │   └── AppConfigTestWithPropertySourceFile.java
    ├── controller/
    │   ├── ExceptionHandlerTest.java
    │   └── UserControllerTest.java
    ├── dto/
    │   └── UserRequestTest.java
    ├── exception/
    │   ├── ErrorResponseTest.java           # Cubre equals/hashCode/canEqual con subclases
    │   └── ExceptionTest.java
    ├── integration/
    │   └── FullIntegrationTest.java         # @SpringBootTest + MockMvc: flujo POST → GET
    ├── model/
    │   ├── AllergyTest.java                 # Cubre ManyToMany, canEqual, equals con ids
    │   └── UserTest.java                    # Cubre subclases Lombok canEqual
    └── service/
        ├── UserServiceExtraTest.java        # Casos borde: blank name, age negativo, paginación
        └── UserServiceTest.java             # Cubre ambas ramas: con repo y sin repo
```

---

## Requisitos Previos

- Java 21
- Maven 3.6+ (o usar el Maven Wrapper incluido)

---

## Instalación y Ejecución

### Compilar

```bash
./mvnw clean compile
# Windows:
mvnw.cmd clean compile
```

### Ejecutar (perfil LOCAL por defecto)

```bash
./mvnw spring-boot:run
```

### Ejecutar con perfil específico

```bash
# DEV
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# PROD
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Con variable de entorno personalizada (PowerShell)

```powershell
$env:DB_PASSWORD="tu_contraseña_segura"
./mvnw spring-boot:run
```

Al arrancar, `DataInitializer` precarga automáticamente **5 alergias** y **30 usuarios** con relaciones ManyToMany en H2.

---

## Endpoints de la API

> Base URL: `http://localhost:8080/api`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api` | Listar usuarios (paginado: `?page=1&size=10`) |
| `GET` | `/api/{id}` | Obtener usuario por ID |
| `GET` | `/api/search?name=X` | Buscar por nombre (case-insensitive) |
| `GET` | `/api/users/allergies` | Usuarios con sus alergias (via JPQL JOIN FETCH → `UserDTO`) |
| `GET` | `/api/config` | Ver configuración del perfil activo |
| `POST` | `/api` | Crear usuario |
| `POST` | `/api/batch` | Crear múltiples usuarios |
| `PUT` | `/api/{id}` | Actualizar usuario (parcial: ignora campos nulos/vacíos) |
| `PATCH` | `/api/{id}/name?name=X` | Actualizar solo el nombre (`@Modifying` + `@Query`) |
| `DELETE` | `/api/{id}` | Eliminar usuario |
| `DELETE` | `/api/all` | Eliminar todos los usuarios |

### Ejemplos rápidos (PowerShell)

```powershell
# Crear usuario con alergia
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api" `
  -ContentType "application/json" `
  -Body '{"name":"Juan","age":25,"allergies":[{"name":"Pollen","severity":2}]}'

# Listar con paginación
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api?page=1&size=5"

# Buscar por nombre
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/search?name=Juan"

# Usuarios con alergias (DTO sin ciclos)
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/users/allergies"

# Actualizar solo el nombre
Invoke-RestMethod -Method Patch -Uri "http://localhost:8080/api/1/name?name=Carlos"
```

---

## Modelo de Datos

### Relación ManyToMany: `User` ↔ `Allergy`

```
users                user_allergies         allergy
─────────────────    ─────────────────    ─────────────────
id (PK)              user_id (FK)         id (PK)
name                 allergy_id (FK)      name
age                                       severity
```

- `User` es el **dueño** de la relación (`@JoinTable`)
- `Allergy` usa `mappedBy` + `@JsonIgnore` para evitar referencias circulares
- Los DTOs (`UserDTO` / `AllergyDTO`) rompen los ciclos en la respuesta JSON

### Entidades

```java
// User — entidad principal
@Entity @Data @Table(name = "users")
public class User {
    @Id @GeneratedValue Long id;
    @NotBlank String name;
    @Min(0) @Max(120) int age;
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "user_allergies", ...)
    List<Allergy> allergies;
}

// Allergy — entidad compartida
@Entity @Data
public class Allergy {
    @Id @GeneratedValue Long id;
    String name;
    int severity;
    @ManyToMany(mappedBy = "allergies") @JsonIgnore
    List<User> users;
}
```

### Queries personalizadas en `UserRepository`

```java
// JPQL con JOIN FETCH para evitar N+1
@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.allergies")
List<User> findAllWithAllergies();

// UPDATE directo sin cargar la entidad
@Modifying @Transactional
@Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
int updateNameById(@Param("id") Long id, @Param("name") String name);
```

---

## Perfiles de Configuración

| Propiedad | LOCAL | DEV | PROD |
|---|---|---|---|
| `app.name` | Mi App GFT - LOCAL | GFT - Entorno DESARROLLO | Mi App GFT REAL |
| `update.disabled` | `false` | `true` | `true` |
| `pagination.max-size` | 50 | 20 | 100 |
| `defaults.age` | 18 | 25 | 18 |

Contraseñas via variable de entorno:
```yaml
password: ${DB_PASSWORD:valor_por_defecto}
```

---

## Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Tests + reporte JaCoCo
./mvnw clean test jacoco:report
# → Abre: target/site/jacoco/index.html

# Verificación completa (tests + quality gate 100%)
./mvnw clean verify
```

### Cobertura 100% — estrategia

| Clase/Capa | Estrategia |
|---|---|
| `UserService` | Prueba ambas ramas: con `UserRepository` mockeado y sin él (lista en memoria) |
| `UserServiceExtraTest` | Casos borde: nombre en blanco, edad negativa, paginación con `size=0` y `size > maxSize` |
| `UserTest` / `AllergyTest` | Cubre `equals`, `hashCode`, `canEqual` con subclases `Lombok` estrictas |
| `ErrorResponseTest` | Ídem para `ErrorResponse` + ramas null |
| `LoggingAspectTest` | Mockea `ProceedingJoinPoint`, cubre rama de excepción y método de pointcut |
| `FullIntegrationTest` | `@SpringBootTest` + `MockMvc`: flujo completo POST → GET con datos reales |
| `AppConfig*` | Múltiples variantes con `@PropertySource` y perfiles |

---

## CI/CD

| Workflow | Archivo | Trigger |
|---|---|---|
| Build + Tests + JaCoCo quality gate | `.github/workflows/ci.yml` | Push / PR |
| SonarCloud análisis estático | `.github/workflows/sonarcloud.yml` | Push |

**Secrets necesarios para SonarCloud:**
- `SONAR_TOKEN`, `SONAR_ORGANIZATION`, `SONAR_PROJECT_KEY`

---

## Principios Aplicados

- **Clean Code** — nombres descriptivos, funciones pequeñas, sin magia hardcodeada
- **SOLID** — responsabilidad única por capa, inversión de dependencias via constructor injection
- **DRY** — lógica centralizada en `UserService`, validaciones en anotaciones Jakarta
- **AOP** — `LoggingAspect` intercepta todos los métodos del paquete principal
- **Layered Architecture** — Controller → Service → Repository
- **DTO pattern** — `UserRequest` de entrada, `UserDTO`/`AllergyDTO` de salida
- **Anti-N+1** — `JOIN FETCH` en query personalizada para alergias

---

## Generar JAR

```bash
./mvnw clean package
java -jar target/GFTFormacion-0.0.1-SNAPSHOT.jar
```

---

## Autor

**Pau López Núñez** — GFT Junior Training Programme (back-end track)  
[github.com/PauLopNun](https://github.com/PauLopNun)

---

## Licencia

Proyecto de uso educativo — GFT Technologies SE.
