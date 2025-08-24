# Script de prueba para operaciones CRUD en Azure Function
$baseUrl = "http://localhost:7071/api/Facturas"

Write-Host "=== Probando Operaciones CRUD ===" -ForegroundColor Green

# 1. GET - Leer todos los documentos
Write-Host "`n1. GET - Leyendo todos los documentos..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "¡Éxito! Encontrados $($response.Count) documentos" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. POST - Crear un nuevo documento
Write-Host "`n2. POST - Creando un nuevo documento..." -ForegroundColor Yellow
$newDocument = @{
    documento_tipo_id = 2
    impuesto_id = 2
    documento_numero = 999
    documento_fecha = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    registro_fecha = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    usuario_id = 2
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $newDocument -ContentType "application/json"
    Write-Host "¡Éxito! Documento creado con ID: $($response.documento_id)" -ForegroundColor Green
    $createdId = $response.documento_id
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    $createdId = 1  # Usar ID existente para pruebas
}

# 3. GET - Leer documento específico
Write-Host "`n3. GET - Leyendo documento con ID: $createdId..." -ForegroundColor Yellow
try {
    $uri = "$baseUrl?id=$createdId"
    $response = Invoke-RestMethod -Uri $uri -Method GET
    Write-Host "¡Éxito! Documento recuperado" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. PUT - Actualizar documento
Write-Host "`n4. PUT - Actualizando documento con ID: $createdId..." -ForegroundColor Yellow
$updateDocument = @{
    documento_tipo_id = 3
    impuesto_id = 3
    documento_numero = 888
    documento_fecha = (Get-Date).AddDays(1).ToString("yyyy-MM-ddTHH:mm:ss")
    usuario_id = 3
} | ConvertTo-Json

try {
    $uri = "$baseUrl?id=$createdId"
    $response = Invoke-RestMethod -Uri $uri -Method PUT -Body $updateDocument -ContentType "application/json"
    Write-Host "¡Éxito! Documento actualizado" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. GET - Leer todos los documentos nuevamente para ver cambios
Write-Host "`n5. GET - Leyendo todos los documentos después de la actualización..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "¡Éxito! Encontrados $($response.Count) documentos" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. DELETE - Eliminar documento (comentado para evitar pérdida de datos)
Write-Host "`n6. DELETE - Eliminando documento con ID: $createdId (OMITIDO por seguridad)..." -ForegroundColor Yellow
Write-Host "Descomenta las siguientes líneas para probar la operación DELETE:" -ForegroundColor Cyan
Write-Host "# try {" -ForegroundColor Gray
Write-Host "#     `$uri = `"$baseUrl?id=$createdId`"" -ForegroundColor Gray
Write-Host "#     Invoke-RestMethod -Uri `$uri -Method DELETE" -ForegroundColor Gray
Write-Host "#     Write-Host `"¡Éxito! Documento eliminado`" -ForegroundColor Green" -ForegroundColor Gray
Write-Host "# } catch {" -ForegroundColor Gray
Write-Host "#     Write-Host `"Error: $($_.Exception.Message)`" -ForegroundColor Red" -ForegroundColor Gray
Write-Host "# }" -ForegroundColor Gray

Write-Host "`n=== Pruebas CRUD Completadas ===" -ForegroundColor Green
