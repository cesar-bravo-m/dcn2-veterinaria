# Azure Function - Operaciones CRUD para vt_documento

Esta Azure Function proporciona operaciones CRUD completas (Crear, Leer, Actualizar, Eliminar) para la tabla `vt_documento` en una base de datos PostgreSQL.

## Conexión a la Base de Datos

- **Host**: 40.67.145.51:5432
- **Base de datos**: duoc
- **Usuario**: duoc
- **Contraseña**: 84oL4mK6cM8w7SK
- **Tabla**: vt_documento

## Endpoints de la API

### URL Base
```
http://localhost:7071/api/Facturas
```

### 1. GET - Operaciones de Lectura

#### Obtener Todos los Documentos
```http
GET /api/Facturas
```

**Respuesta**: Array de todos los documentos en formato JSON

#### Obtener Documento por ID
```http
GET /api/Facturas?id={documento_id}
```

**Respuesta**: Documento individual en formato JSON

### 2. POST - Operación de Creación

#### Crear Nuevo Documento
```http
POST /api/Facturas
Content-Type: application/json

{
  "documento_tipo_id": 2,
  "impuesto_id": 2,
  "documento_numero": 999,
  "documento_fecha": "2025-08-24T17:30:00",
  "registro_fecha": "2025-08-24T17:30:00",
  "usuario_id": 2
}
```

**Respuesta**: Documento creado con `documento_id` generado

### 3. PUT - Operación de Actualización

#### Actualizar Documento Existente
```http
PUT /api/Facturas?id={documento_id}
Content-Type: application/json

{
  "documento_tipo_id": 3,
  "impuesto_id": 3,
  "documento_numero": 888,
  "documento_fecha": "2025-08-25T17:30:00",
  "usuario_id": 3
}
```

**Respuesta**: Documento actualizado

### 4. DELETE - Operación de Eliminación

#### Eliminar Documento por ID
```http
DELETE /api/Facturas?id={documento_id}
```

**Respuesta**: 204 No Content en caso de éxito

## Modelo de Datos

### DocumentoDTO
```json
{
  "documento_id": 1,
  "documento_tipo_id": 1,
  "impuesto_id": 1,
  "documento_numero": 1,
  "documento_fecha": "2025-08-24T17:01:08.523763",
  "registro_fecha": "2025-08-24T17:01:08.523763",
  "usuario_id": 1
}
```

### Descripción de Campos
- `documento_id`: Clave primaria (auto-generada)
- `documento_tipo_id`: Clave foránea a vt_documento_tipo
- `impuesto_id`: Clave foránea a vt_impuesto
- `documento_numero`: Número de documento
- `documento_fecha`: Fecha del documento (nullable)
- `registro_fecha`: Fecha de registro (auto-establecida si no se proporciona)
- `usuario_id`: Clave foránea a vt_usuario

## Pruebas

### Script de Prueba PowerShell
Ejecuta el script de prueba incluido para verificar todas las operaciones CRUD:

```powershell
.\test-crud.ps1
```

### Ejemplos de Pruebas Manuales

#### Obtener todos los documentos
```powershell
Invoke-RestMethod -Uri "http://localhost:7071/api/Facturas" -Method GET
```

#### Obtener documento por ID
```powershell
Invoke-RestMethod -Uri "http://localhost:7071/api/Facturas?id=1" -Method GET
```

#### Crear nuevo documento
```powershell
$newDoc = @{
    documento_tipo_id = 2
    impuesto_id = 2
    documento_numero = 999
    documento_fecha = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    registro_fecha = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    usuario_id = 2
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:7071/api/Facturas" -Method POST -Body $newDoc -ContentType "application/json"
```

#### Actualizar documento
```powershell
$updateDoc = @{
    documento_tipo_id = 3
    impuesto_id = 3
    documento_numero = 888
    documento_fecha = (Get-Date).AddDays(1).ToString("yyyy-MM-ddTHH:mm:ss")
    usuario_id = 3
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:7071/api/Facturas?id=1" -Method PUT -Body $updateDoc -ContentType "application/json"
```

#### Eliminar documento
```powershell
Invoke-RestMethod -Uri "http://localhost:7071/api/Facturas?id=1" -Method DELETE
```

## Manejo de Errores

La API devuelve códigos de estado HTTP apropiados:

- `200 OK`: Operaciones GET/PUT exitosas
- `201 Created`: Operación POST exitosa
- `204 No Content`: Operación DELETE exitosa
- `400 Bad Request`: Formato de solicitud o parámetros inválidos
- `404 Not Found`: Documento no encontrado
- `500 Internal Server Error`: Error de conexión a base de datos o del servidor

## Dependencias

- Azure Functions Java Library 3.1.0
- PostgreSQL JDBC Driver 42.7.1
- Jackson Databind 2.15.2
- Jackson JSR310 2.15.2 (para soporte de fecha/hora de Java 8)

## Desarrollo

### Compilación
```bash
mvn clean compile
```

### Ejecución Local
```bash
mvn azure-functions:run
```

### Pruebas
```bash
mvn test
```

### Empaquetado
```bash
mvn package
```

## Despliegue

La función se puede desplegar en Azure Functions usando el Azure Functions Maven Plugin:

```bash
mvn azure-functions:deploy
```

## Notas

- La función establece automáticamente `registro_fecha` al timestamp actual si no se proporciona en las solicitudes POST
- Todos los campos de fecha/hora se manejan en formato ISO 8601
- La función incluye manejo de errores y logging apropiados
- Las conexiones a la base de datos se gestionan correctamente con try-with-resources
