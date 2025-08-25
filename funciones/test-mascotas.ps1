# Test script for Mascotas CRUD operations
# Make sure the Azure Function is running locally or deployed

$baseUrl = "http://localhost:7071/api/Mascotas"

Write-Host "=== Testing Mascotas CRUD Operations ===" -ForegroundColor Green

# Test 1: Get all mascotas
Write-Host "`n1. Getting all mascotas..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl" -Method GET
    Write-Host "Success! Found $($response.Count) mascotas" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a new mascota
Write-Host "`n2. Creating a new mascota..." -ForegroundColor Yellow
$newMascota = @{
    cliente_id = 1
    nombre = "Luna"
    especie = "Perro"
    edad = 3
    domestico = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl" -Method POST -Body $newMascota -ContentType "application/json"
    Write-Host "Success! Created mascota with ID: $($response.mascota_id)" -ForegroundColor Green
    $mascotaId = $response.mascota_id
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    $mascotaId = 1  # Fallback for testing
}

# Test 3: Get mascota by ID
Write-Host "`n3. Getting mascota by ID: $mascotaId..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$mascotaId" -Method GET
    Write-Host "Success! Retrieved mascota:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get mascotas by cliente_id
Write-Host "`n4. Getting mascotas by cliente_id: 1..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?cliente_id=1" -Method GET
    Write-Host "Success! Found $($response.Count) mascotas for cliente 1" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Update mascota
Write-Host "`n5. Updating mascota ID: $mascotaId..." -ForegroundColor Yellow
$updateMascota = @{
    cliente_id = 1
    nombre = "Luna Updated"
    especie = "Perro"
    edad = 4
    domestico = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$mascotaId" -Method PUT -Body $updateMascota -ContentType "application/json"
    Write-Host "Success! Updated mascota:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Delete mascota
Write-Host "`n6. Deleting mascota ID: $mascotaId..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$mascotaId" -Method DELETE
    Write-Host "Success! Mascota deleted" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Verify deletion
Write-Host "`n7. Verifying deletion..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$mascotaId" -Method GET
    Write-Host "Warning: Mascota still exists" -ForegroundColor Yellow
} catch {
    Write-Host "Success! Mascota was deleted (404 expected)" -ForegroundColor Green
}

Write-Host "`n=== Test completed ===" -ForegroundColor Green
