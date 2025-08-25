# Test script for Laboratorio CRUD operations
# Make sure the Azure Function is running locally first

$baseUrl = "http://localhost:7071/api/Laboratorio"

Write-Host "=== Testing Laboratorio CRUD Operations ===" -ForegroundColor Green

# Test 1: Create a new laboratory record
Write-Host "`n1. Creating a new laboratory record..." -ForegroundColor Yellow
$createData = @{
    mascota_id = 1
    examen = "Hemograma"
    resultado = "Valores normales, glóbulos rojos y blancos dentro de rangos esperados"
    valor = 12.5
    unidad = "g/dL"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $createData -ContentType "application/json"
    Write-Host "Created laboratory record:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
    $laboratorioId = $response.laboratorio_id
} catch {
    Write-Host "Error creating laboratory record: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Get all laboratory records
Write-Host "`n2. Getting all laboratory records..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "All laboratory records:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting all laboratory records: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get laboratory record by ID
Write-Host "`n3. Getting laboratory record by ID: $laboratorioId" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$laboratorioId" -Method GET
    Write-Host "Laboratory record by ID:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting laboratory record by ID: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get laboratory records by mascota_id
Write-Host "`n4. Getting laboratory records by mascota_id: 1" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?mascota_id=1" -Method GET
    Write-Host "Laboratory records by mascota_id:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting laboratory records by mascota_id: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get laboratory records by examen
Write-Host "`n5. Getting laboratory records by examen: Hemograma" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?examen=Hemograma" -Method GET
    Write-Host "Laboratory records by examen:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting laboratory records by examen: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Update laboratory record
Write-Host "`n6. Updating laboratory record ID: $laboratorioId" -ForegroundColor Yellow
$updateData = @{
    mascota_id = 1
    examen = "Hemograma Completo"
    resultado = "Valores normales, glóbulos rojos y blancos dentro de rangos esperados. Plaquetas normales."
    valor = 12.8
    unidad = "g/dL"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$laboratorioId" -Method PUT -Body $updateData -ContentType "application/json"
    Write-Host "Updated laboratory record:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error updating laboratory record: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Create another laboratory record for testing
Write-Host "`n7. Creating another laboratory record..." -ForegroundColor Yellow
$createData2 = @{
    mascota_id = 2
    examen = "Radiografía"
    resultado = "No se observan fracturas. Estructura ósea normal."
    valor = $null
    unidad = $null
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $createData2 -ContentType "application/json"
    Write-Host "Created second laboratory record:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
    $laboratorioId2 = $response.laboratorio_id
} catch {
    Write-Host "Error creating second laboratory record: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Get all laboratory records again
Write-Host "`n8. Getting all laboratory records after updates..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "All laboratory records:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting all laboratory records: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Delete laboratory record
Write-Host "`n9. Deleting laboratory record ID: $laboratorioId2" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$laboratorioId2" -Method DELETE
    Write-Host "Deleted laboratory record successfully" -ForegroundColor Green
} catch {
    Write-Host "Error deleting laboratory record: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: Verify deletion
Write-Host "`n10. Verifying deletion by trying to get deleted record..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$laboratorioId2" -Method GET
    Write-Host "Record still exists (unexpected):" -ForegroundColor Yellow
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Record successfully deleted (expected error): $($_.Exception.Message)" -ForegroundColor Green
}

Write-Host "`n=== Laboratorio CRUD Tests Completed ===" -ForegroundColor Green
