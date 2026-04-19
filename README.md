# Sistema Gestor de Incidencias

Backend REST API desarrollado con **Spring Boot 4** y **Java 21** para la gestión de incidencias técnicas dentro de una organización. Permite reportar problemas, asignarlos a técnicos y hacer seguimiento del historial de atención.

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.5 | Framework backend |
| Spring Web MVC | 4.x | Exposición de APIs REST |
| Jackson | 2.x | Serialización JSON |
| JUnit 5 | 5.x | Framework de tests |
| Mockito | 5.x | Mocking en tests |
| MockMvc | 4.x | Tests de controllers |
| Maven | 3.x | Gestión de dependencias |

---

## Estructura del proyecto

```
src/
├── main/
│   └── java/utp/edu/sistema_gestor_incidencias/
│       ├── SistemaGestorIncidenciasApplication.java
│       ├── controller/
│       │   ├── UsuarioController.java
│       │   ├── IncidenteController.java
│       │   └── SeguimientoController.java
│       ├── service/
│       │   ├── UsuarioService.java
│       │   ├── IncidenteService.java
│       │   └── SeguimientoService.java
│       └── model/
│           ├── Usuario.java
│           ├── Incidencia.java
│           ├── Seguimiento.java
│           ├── Rol.java
│           ├── Area.java
│           ├── Estado.java
│           └── EstadoIncidencia.java
└── test/
    └── java/utp/edu/sistema_gestor_incidencias/
        ├── SistemaGestorIncidenciasApplicationTests.java
        └── controller/
            ├── UsuarioControllerTest.java
            ├── IncidenteControllerTest.java
            └── SeguimientoControllerTest.java
```

---

## Modelos

### Usuario
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nombre` | `String` | Nombre completo |
| `correo` | `String` | Correo electrónico |
| `rol` | `Rol` | Rol del usuario |
| `area` | `Area` | Área de trabajo |
| `estado` | `Estado` | Estado activo/inactivo |

### Incidencia
| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único |
| `titulo` | `String` | Título del problema |
| `descripcion` | `String` | Detalle del problema |
| `estado` | `EstadoIncidencia` | Estado actual |
| `usuario` | `Usuario` | Quien reportó |
| `tecnico` | `Usuario` | Quien atiende |

### Seguimiento
| Campo | Tipo | Descripción |
|---|---|---|
| `idSeguimiento` | `Long` | Identificador único |
| `incidencia` | `Incidencia` | Incidencia relacionada |
| `comentario` | `String` | Acción realizada |
| `fecha` | `Date` | Fecha del registro |
| `estado` | `Estado` | Estado del seguimiento |

### Enums

```java
enum Rol            { ADMIN, EMPLEADO, TECNICO_NIVEL_1, TECNICO_NIVEL_2, TECNICO_NIVEL_3 }
enum Area           { RRHH, ADMINISTRACION, CONTABILIDAD, GERENCIA, LOGISTICA }
enum Estado         { ACTIVO, INACTIVO }
enum EstadoIncidencia { ABIERTO, PENDIENTE, CERRADO }
```

---

## Endpoints de la API

### Usuarios — `/api/usuario`

| Método | Endpoint | Descripción | HTTP Response |
|---|---|---|---|
| `POST` | `/api/usuario` | Crear usuario | `201 Created` |
| `PUT` | `/api/usuario/{id}` | Modificar usuario | `200 OK` / `404 Not Found` |
| `GET` | `/api/usuario` | Listar todos | `200 OK` |
| `GET` | `/api/usuario/{id}` | Obtener por ID | `200 OK` / `404 Not Found` |

### Incidencias — `/api/incidencia`

| Método | Endpoint | Descripción | HTTP Response |
|---|---|---|---|
| `POST` | `/api/incidencia` | Crear incidencia | `201 Created` |
| `PUT` | `/api/incidencia/{id}` | Modificar incidencia | `200 OK` / `404 Not Found` |
| `GET` | `/api/incidencia` | Listar todas | `200 OK` |
| `GET` | `/api/incidencia/{id}` | Obtener por ID | `200 OK` / `404 Not Found` |

### Seguimiento — `/api/seguimiento`

| Método | Endpoint | Descripción | HTTP Response |
|---|---|---|---|
| `POST` | `/api/seguimiento` | Crear seguimiento | `201 Created` |
| `PUT` | `/api/seguimiento/{id}` | Modificar seguimiento | `200 OK` / `404 Not Found` |
| `GET` | `/api/seguimiento` | Listar todos | `200 OK` |
| `GET` | `/api/seguimiento/{id}` | Obtener por ID | `200 OK` / `404 Not Found` |

---

## Ejemplos de uso

### Crear un usuario
```http
POST http://localhost:8080/api/usuario
Content-Type: application/json

{
  "nombre": "Abel Torres",
  "correo": "abel@utp.edu",
  "rol": "EMPLEADO",
  "area": "SISTEMAS",
  "estado": "ACTIVO"
}
```

### Crear una incidencia
```http
POST http://localhost:8080/api/incidencia
Content-Type: application/json

{
  "titulo": "PC no enciende",
  "descripcion": "El equipo no responde al inicio",
  "estado": "ABIERTO",
  "usuario": { "id": 1 },
  "tecnico": { "id": 2 }
}
```

### Registrar un seguimiento
```http
POST http://localhost:8080/api/seguimiento
Content-Type: application/json

{
  "incidencia": { "id": 1 },
  "comentario": "Revisando el equipo en sitio",
  "estado": "ACTIVO"
}
```

### Modificar estado de una incidencia
```http
PUT http://localhost:8080/api/incidencia/1
Content-Type: application/json

{
  "titulo": "PC no enciende",
  "descripcion": "El equipo no responde al inicio",
  "estado": "PENDIENTE",
  "tecnico": { "id": 2 }
}
```

---

## Tests

El proyecto usa **TDD (Test Driven Development)** con MockMvc y Mockito. Los tests cubren el comportamiento de cada endpoint verificando códigos HTTP y estructura del JSON de respuesta.

### Ejecutar tests
```bash
./mvnw test
```

### Cobertura de tests

| Clase de Test | Tests | Responsable |
|---|---|---|
| `UsuarioControllerTest` | crear, modificar, listar, obtener, 404 | Abel + Jaime |
| `IncidenteControllerTest` | crear, modificar, listar, obtener, 404 | Jaime + Johan |
| `SeguimientoControllerTest` | crear, modificar, listar, obtener, 404 | Stephani + Jamil |

**Total: 15 tests**

### Qué verifica cada test
- **crear** → retorna `201 Created` con el objeto creado y su ID asignado
- **modificar** → retorna `200 OK` con los datos actualizados
- **listar** → retorna `200 OK` con array y longitud correcta
- **obtener por ID (existe)** → retorna `200 OK` con el objeto
- **obtener por ID (no existe)** → retorna `404 Not Found`

---

## Cómo ejecutar el proyecto

### Requisitos
- Java 21+
- Maven 3.x (o usar el wrapper `./mvnw`)

### Pasos
```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd backend_sistema_gestor_incidencias

# Ejecutar la aplicación
./mvnw spring-boot:run

# La API estará disponible en:
# http://localhost:8080
```

---

## Ramas

| Rama | Descripción |
|---|---|
| `main` | Código principal estable |
| `feature/tests-controllers` | Tests MockMvc para los 3 controllers |

---

## Equipo de desarrollo

| Integrante | Módulo asignado |
|---|---|
| Abel | Usuarios — crear, modificar, listar |
| Jaime | Usuarios — listar uno / Incidencias — crear, modificar |
| Johan | Incidencias — listar, listar uno |
| Stephani | Seguimiento — crear, modificar |
| Jamil | Seguimiento — listar, listar uno |

---

## Notas técnicas

- Los datos se almacenan **en memoria** (sin base de datos). Al reiniciar el servidor los datos se pierden.
- Los IDs se generan automáticamente con `AtomicLong` de forma thread-safe.
- Arquitectura en capas: **Controller → Service** (patrón MVC sin capa Repository por ahora).
- Próximo paso recomendado: integrar **Spring Data JPA + PostgreSQL** para persistencia real.
