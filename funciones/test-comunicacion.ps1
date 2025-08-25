# Test script for Comunicacion CRUD Function
# Make sure the function is running locally before executing these tests

$baseUrl = "http://localhost:7071/api/Comunicacion"

Write-Host "=== Testing Comunicacion CRUD Function ===" -ForegroundColor Green

# Test 1: Create a new message
Write-Host "`n1. Creating a new message..." -ForegroundColor Yellow
$newMessage = @{
    cliente_id = 1
    contenido = "Hola, este es un mensaje de prueba"
    estado = "ENVIADO"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method POST -Body $newMessage -ContentType "application/json"
    Write-Host "Message created successfully:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
    $messageId = $response.mensaje_id
} catch {
    Write-Host "Error creating message: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Get all messages
Write-Host "`n2. Getting all messages..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method GET
    Write-Host "All messages retrieved successfully. Count: $($response.Count)" -ForegroundColor Green
} catch {
    Write-Host "Error getting all messages: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get message by ID
Write-Host "`n3. Getting message by ID: $messageId" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$messageId" -Method GET
    Write-Host "Message retrieved successfully:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error getting message by ID: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get messages by cliente_id
Write-Host "`n4. Getting messages by cliente_id: 1" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?cliente_id=1" -Method GET
    Write-Host "Messages by cliente_id retrieved successfully. Count: $($response.Count)" -ForegroundColor Green
} catch {
    Write-Host "Error getting messages by cliente_id: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get messages by estado
Write-Host "`n5. Getting messages by estado: ENVIADO" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?estado=ENVIADO" -Method GET
    Write-Host "Messages by estado retrieved successfully. Count: $($response.Count)" -ForegroundColor Green
} catch {
    Write-Host "Error getting messages by estado: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Update message
Write-Host "`n6. Updating message ID: $messageId" -ForegroundColor Yellow
$updateMessage = @{
    cliente_id = 1
    contenido = "Mensaje actualizado - contenido modificado"
    estado = "ENTREGADO"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$messageId" -Method PUT -Body $updateMessage -ContentType "application/json"
    Write-Host "Message updated successfully:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error updating message: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Delete message
Write-Host "`n7. Deleting message ID: $messageId" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$messageId" -Method DELETE
    Write-Host "Message deleted successfully" -ForegroundColor Green
} catch {
    Write-Host "Error deleting message: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Verify deletion
Write-Host "`n8. Verifying message deletion..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?id=$messageId" -Method GET
    Write-Host "Message still exists (unexpected)" -ForegroundColor Red
} catch {
    Write-Host "Message successfully deleted (404 expected)" -ForegroundColor Green
}

Write-Host "`n=== All tests completed ===" -ForegroundColor Green
