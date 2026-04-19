# Casos de Prueba de Seguimiento

Este documento describe casos de prueba funcionales para los endpoints implementados en el modulo de seguimiento.

Base URL local:

```text
http://localhost:8080
```

## Endpoint: Crear seguimiento

Ruta:

```text
POST /api/seguimiento
```

### Caso 1. Crear seguimiento con incidencia valida

- Entrada:

```json
{
  "comentario": "Se valida el problema reportado",
  "fecha": "2026-04-17T10:00:00.000+00:00",
  "estado": "activo",
  "incidencia": {
    "id": 1
  }
}
```

- Salida esperada:
  - Codigo HTTP `200 OK`.
  - Se devuelve un objeto `Seguimiento` con `idSeguimiento` generado.
  - El campo `incidencia.id` debe ser `1`.
- Verificacion:
  - Confirmar que la respuesta contiene `comentario = "Se valida el problema reportado"`.
  - Confirmar que `idSeguimiento` exista y sea numerico.
  - Confirmar que `estado = "activo"`.

### Caso 2. Crear seguimiento con incidencia inexistente

- Entrada:

```json
{
  "comentario": "Seguimiento sobre incidencia no registrada",
  "fecha": "2026-04-17T10:00:00.000+00:00",
  "estado": "activo",
  "incidencia": {
    "id": 99
  }
}
```

- Salida esperada:
  - Codigo HTTP `404 Not Found`.
  - Mensaje: `Incidencia no encontrada con ese ID`.
- Verificacion:
  - Confirmar que no se crea ningun seguimiento.
  - Confirmar que el cuerpo de respuesta coincide con el mensaje de error esperado.

### Caso 3. Crear seguimiento sin incidencia

- Entrada:

```json
{
  "comentario": "Seguimiento incompleto",
  "fecha": "2026-04-17T10:00:00.000+00:00",
  "estado": "activo"
}
```

- Salida esperada:
  - Codigo HTTP `400 Bad Request`.
  - Mensaje: `Debe enviar el ID de la incidencia`.
- Verificacion:
  - Confirmar que la API rechaza la solicitud.
  - Confirmar que el error corresponde a falta de referencia de incidencia.

## Endpoint: Modificar seguimiento

Ruta:

```text
POST /api/seguimiento/{id}
```

### Caso 1. Modificar seguimiento existente con datos validos

- Precondicion:
  - Debe existir un seguimiento con ID `1`.
  - Debe existir una incidencia con ID `1`.
- Entrada:

```json
{
  "comentario": "Seguimiento actualizado despues de revision",
  "fecha": "2026-04-17T11:00:00.000+00:00",
  "estado": "inactivo",
  "incidencia": {
    "id": 1
  }
}
```

- Salida esperada:
  - Codigo HTTP `200 OK`.
  - Se devuelve el seguimiento actualizado con `idSeguimiento = 1`.
- Verificacion:
  - Confirmar que `comentario` cambia al nuevo valor.
  - Confirmar que `estado = "inactivo"`.
  - Confirmar que la referencia de incidencia sigue siendo valida.

### Caso 2. Modificar seguimiento inexistente

- Entrada:

```json
{
  "comentario": "Intento de actualizar seguimiento no existente",
  "fecha": "2026-04-17T11:00:00.000+00:00",
  "estado": "activo",
  "incidencia": {
    "id": 1
  }
}
```

- URL de prueba:

```text
POST /api/seguimiento/99
```

- Salida esperada:
  - Codigo HTTP `404 Not Found`.
  - Mensaje: `Seguimiento no encontrado con ese ID`.
- Verificacion:
  - Confirmar que no se actualiza ningun registro.
  - Confirmar que el mensaje de respuesta coincide con el esperado.

### Caso 3. Modificar seguimiento usando incidencia inexistente

- Entrada:

```json
{
  "comentario": "Actualizacion con incidencia invalida",
  "fecha": "2026-04-17T11:00:00.000+00:00",
  "estado": "activo",
  "incidencia": {
    "id": 999
  }
}
```

- Salida esperada:
  - Codigo HTTP `404 Not Found`.
  - Mensaje: `Incidencia no encontrada con ese ID`.
- Verificacion:
  - Confirmar que la API no actualiza el seguimiento.
  - Confirmar que la validacion de incidencia se ejecuta antes de persistir cambios.
