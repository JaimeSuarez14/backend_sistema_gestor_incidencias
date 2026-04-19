# Sistema Gestor de Incidencias - Backend

Backend desarrollado con Spring Boot para gestionar usuarios e incidencias dentro de una organizacion. El proyecto expone una API REST sencilla, orientada a registrar usuarios, consultar usuarios por ID y crear o modificar incidencias asignadas a tecnicos.

## Objetivo

Este proyecto sirve como base para un sistema de mesa de ayuda o soporte interno. Su proposito es centralizar el registro de incidencias reportadas por usuarios y asignarlas a personal tecnico para su atencion.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.0.5
- Maven Wrapper
- API REST con Spring Web MVC

## Estado actual del proyecto

La aplicacion se encuentra en una fase basica de desarrollo:

- Usa almacenamiento en memoria mediante listas Java.
- No tiene base de datos.
- No incorpora autenticacion ni autorizacion.
- No implementa persistencia entre reinicios.
- El modulo de seguimiento existe como estructura, pero aun no expone operaciones funcionales.

## Estructura principal

```text
src/main/java/utp/edu/sistema_gestor_incidencias
|-- controller
|   |-- UsuarioController.java
|   |-- IncidenciaController.java
|   `-- SeguimientoController.java
|-- service
|   |-- UsuarioService.java
|   `-- IncidenciaService.java
|-- model
|   |-- Usuario.java
|   |-- Incidencia.java
|   |-- Seguimiento.java
|   |-- Rol.java
|   |-- Area.java
|   |-- Estado.java
|   `-- EstadoIncidencia.java
`-- SistemaGestorIncidenciasApplication.java
```

## Endpoints disponibles

### Usuarios

- `POST /api/usuario`
  - Crea un nuevo usuario.
- `GET /api/usuario/{id}`
  - Obtiene un usuario por su identificador.

Ejemplo de cuerpo para crear un usuario:

```json
{
  "nombre": "Juan Perez",
  "correo": "juan@empresa.com",
  "estado": "activo",
  "rol": "empleado",
  "area": "Administracion"
}
```

### Incidencias

- `POST /api/incidencia`
  - Registra una nueva incidencia.
- `POST /api/incidencia/{id}`
  - Modifica una incidencia existente segun su ID.

Ejemplo de cuerpo para crear una incidencia:

```json
{
  "titulo": "Error de acceso al sistema",
  "descripcion": "El usuario no puede iniciar sesion",
  "estado": "abierto",
  "usuario": {
    "id": 1
  },
  "tecnico": {
    "id": 2
  }
}
```

Importante:

- El usuario reportante y el tecnico deben existir previamente.
- Si alguno de los IDs no existe, la API responde con `404 Not Found`.

## Como ejecutar el proyecto

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux o macOS

```bash
./mvnw spring-boot:run
```

Por defecto, la aplicacion se ejecuta en:

```text
http://localhost:8080
```

## Como ejecutar pruebas

```powershell
.\mvnw.cmd test
```

## Limitaciones actuales

- Los datos se pierden al detener la aplicacion.
- No hay validaciones avanzadas de entrada.
- No existen endpoints para listar usuarios o incidencias.
- El controlador de seguimiento aun no implementa operaciones REST.
- La actualizacion de incidencias devuelve `null` si no encuentra el ID, sin manejo explicito de error.

## Siguiente evolucion recomendada

- Integrar Spring Data JPA con una base de datos relacional.
- Agregar validaciones con Bean Validation.
- Implementar operaciones completas de seguimiento.
- Incorporar manejo global de errores.
- Separar DTOs de las entidades de dominio.
- Agregar pruebas unitarias e integracion para los endpoints.

## Documentacion ampliada

La explicacion detallada del proyecto se encuentra en [DOCUMENTACION_PROYECTO.md](DOCUMENTACION_PROYECTO.md).
