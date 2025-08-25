# Azure Functions CRUD Postman Collection

This Postman collection provides comprehensive testing for all CRUD operations across all Azure Functions in the project.

## 📋 Collection Overview

The collection includes **5 main function groups** with **25 total requests**:

### 1. **Clientes** (5 requests)
- Get All Clientes
- Get Cliente by ID
- Create Cliente
- Update Cliente
- Delete Cliente

### 2. **Mascotas** (6 requests)
- Get All Mascotas
- Get Mascota by ID
- Get Mascotas by Cliente ID
- Create Mascota
- Update Mascota
- Delete Mascota

### 3. **Laboratorio** (7 requests)
- Get All Laboratorios
- Get Laboratorio by ID
- Get Laboratorios by Mascota ID
- Get Laboratorios by Examen
- Create Laboratorio
- Update Laboratorio
- Delete Laboratorio

### 4. **Facturas** (5 requests)
- Get All Facturas
- Get Factura by ID
- Create Factura
- Update Factura
- Delete Factura

### 5. **Comunicacion** (7 requests)
- Get All Mensajes
- Get Mensaje by ID
- Get Mensajes by Cliente ID
- Get Mensajes by Estado
- Create Mensaje
- Update Mensaje
- Delete Mensaje

## 🚀 Setup Instructions

### 1. Import the Collection
1. Open Postman
2. Click **Import** button
3. Select the `Azure-Functions-CRUD.postman_collection.json` file
4. The collection will be imported with all requests organized by function

### 2. Configure Environment Variables
The collection uses a `baseUrl` variable that defaults to `http://localhost:7071` for local development.

**To change the base URL:**
1. Click on the collection name
2. Go to the **Variables** tab
3. Update the `baseUrl` value:
   - **Local development**: `http://localhost:7071`
   - **Azure deployment**: `https://your-function-app.azurewebsites.net`

### 3. Start Your Azure Functions
Before testing, make sure your Azure Functions are running:

```bash
# Navigate to your project directory
cd funciones

# Start the functions locally
func start
```

## 📝 Request Details

### Clientes Endpoints
- **Base URL**: `{{baseUrl}}/api/Clientes`
- **Required fields for POST/PUT**: `nombre`, `paterno`, `materno`, `rut`

### Mascotas Endpoints
- **Base URL**: `{{baseUrl}}/api/Mascotas`
- **Required fields for POST/PUT**: `cliente_id`, `nombre`, `especie`, `edad`, `domestico`
- **Query parameters**: `id`, `cliente_id`

### Laboratorio Endpoints
- **Base URL**: `{{baseUrl}}/api/Laboratorio`
- **Required fields for POST/PUT**: `mascota_id`, `examen`, `resultado`
- **Query parameters**: `id`, `mascota_id`, `examen`

### Facturas Endpoints
- **Base URL**: `{{baseUrl}}/api/Facturas`
- **Required fields for POST/PUT**: `documento_tipo_id`, `impuesto_id`, `documento_numero`, `usuario_id`
- **Query parameters**: `id`

### Comunicacion Endpoints
- **Base URL**: `{{baseUrl}}/api/Comunicacion`
- **Required fields for POST/PUT**: `cliente_id`, `contenido`, `estado`
- **Query parameters**: `id`, `cliente_id`, `estado`
- **Status values**: `ENVIADO`, `ENTREGADO`, `LEÍDO`, `ERROR`

## 🧪 Testing Workflow

### Recommended Testing Order
1. **Start with Clientes** - Create a client first
2. **Then Mascotas** - Create pets for the client
3. **Then Laboratorio** - Create lab records for pets
4. **Then Facturas** - Create documents/invoices
5. **Finally Comunicacion** - Create messages for clients

### Example Testing Sequence
1. **Create Cliente** → Note the returned `cliente_id`
2. **Create Mascota** → Use the `cliente_id` from step 1
3. **Create Laboratorio** → Use the `mascota_id` from step 2
4. **Create Mensaje** → Use the `cliente_id` from step 1
5. **Test GET operations** → Retrieve the created records
6. **Test UPDATE operations** → Modify the records
7. **Test DELETE operations** → Clean up the test data

## 📊 Expected Responses

### Success Responses
- **GET (single item)**: Returns JSON object
- **GET (multiple items)**: Returns JSON array
- **POST**: Returns created object with generated ID
- **PUT**: Returns updated object
- **DELETE**: Returns success message

### Error Responses
- **400 Bad Request**: Missing required fields or invalid data
- **404 Not Found**: Record not found
- **500 Internal Server Error**: Database connection issues

## 🔧 Customization

### Adding New Requests
1. Right-click on a function folder
2. Select **Add Request**
3. Configure the request with appropriate method, URL, and body

### Modifying Request Bodies
Each request includes sample JSON data. Modify the request bodies to match your specific test scenarios.

### Environment Variables
You can add more environment variables for different scenarios:
- `testClienteId`: For testing with specific client IDs
- `testMascotaId`: For testing with specific pet IDs
- `testLaboratorioId`: For testing with specific lab record IDs

## 🐛 Troubleshooting

### Common Issues
1. **Connection refused**: Make sure Azure Functions are running
2. **404 errors**: Check the function names and URLs
3. **500 errors**: Check database connection and table existence
4. **Validation errors**: Ensure all required fields are provided

### Debug Tips
1. Check the Azure Functions console for error logs
2. Verify database connection settings
3. Ensure all required tables exist in the database
4. Check that foreign key relationships are properly set up

## 📚 Related Documentation
- [README-Comunicacion.md](./README-Comunicacion.md) - Detailed Comunicacion function documentation
- [README-Mascotas.md](./README-Mascotas.md) - Mascotas function documentation
- [README-Laboratorio.md](./README-Laboratorio.md) - Laboratorio function documentation

## 🤝 Contributing
To add new endpoints or modify existing ones:
1. Update the corresponding Azure Function
2. Add new requests to this collection
3. Update this README with new endpoint documentation
