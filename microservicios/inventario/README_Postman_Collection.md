# Vet API Inventario - Postman Collection

This Postman collection provides a complete set of API endpoints for testing the Veterinary Inventory Management API.

## 📋 Collection Overview

The collection includes the following endpoints:

1. **Get All Inventory Items** - `GET /api/inventario`
2. **Get Inventory Item by ID** - `GET /api/inventario/{id}`
3. **Create New Inventory Item** - `POST /api/inventario`
4. **Update Inventory Item** - `PUT /api/inventario/{id}`
5. **Delete Inventory Item** - `DELETE /api/inventario/{id}`

## 🚀 How to Import

1. Open Postman
2. Click on "Import" button
3. Select the `Vet_API_Inventario.postman_collection.json` file
4. The collection will be imported with all endpoints ready to use

## ⚙️ Environment Variables

The collection uses the following variables:

- `baseUrl`: The base URL of your API (default: `http://localhost:8081`)
- `inventarioId`: The ID of an inventory item for testing (default: `1`)

### Setting up Environment Variables

1. In Postman, click on the "Environment" dropdown (top right)
2. Click "Add" to create a new environment
3. Add the following variables:
   - `baseUrl`: `http://localhost:8081` (or your server URL)
   - `inventarioId`: `1` (or any valid inventory ID)

## 📊 Data Model

The inventory items have the following structure:

```json
{
    "inventarioId": 1,
    "inventarioTipoId": 1,
    "nombre": "Product Name",
    "medidaUnidadId": 1,
    "stock": 100,
    "usuarioId": 1
}
```

### Field Descriptions

- `inventarioId`: Unique identifier (auto-generated)
- `inventarioTipoId`: Type of inventory item
- `nombre`: Product name
- `medidaUnidadId`: Unit of measurement ID
- `stock`: Current stock quantity
- `usuarioId`: User ID associated with the item

## 🧪 Testing Workflow

### 1. Start the Application
Make sure your Spring Boot application is running on port 8081 (or update the `baseUrl` variable accordingly).

### 2. Test the Endpoints

#### Create an Item
1. Use the "Create New Inventory Item" request
2. Modify the JSON body with your desired values
3. Send the request
4. Note the returned `inventarioId` for subsequent tests

#### Get All Items
1. Use the "Get All Inventory Items" request
2. This will return all inventory items in the database

#### Get Specific Item
1. Update the `inventarioId` variable with a valid ID
2. Use the "Get Inventory Item by ID" request

#### Update an Item
1. Set the `inventarioId` variable to the ID you want to update
2. Use the "Update Inventory Item" request
3. Modify the JSON body with new values

#### Delete an Item
1. Set the `inventarioId` variable to the ID you want to delete
2. Use the "Delete Inventory Item" request

## 🔧 Configuration

### Server Configuration
The application is configured to run on port 8081 by default. If you change the port in `application.properties`, update the `baseUrl` variable accordingly.

### Database
The application connects to a PostgreSQL database. Make sure the database is accessible and the connection details in `application.properties` are correct.

## 📝 Notes

- All requests use JSON format for request/response bodies
- The `Content-Type` header is automatically set to `application/json`
- The collection includes descriptions for each endpoint
- You can save responses and add them to the collection for future reference

## 🐛 Troubleshooting

1. **Connection Refused**: Make sure the Spring Boot application is running
2. **404 Not Found**: Verify the `baseUrl` is correct and includes the port number
3. **500 Internal Server Error**: Check the application logs for database connection issues
4. **Invalid JSON**: Ensure the request body follows the correct JSON structure

## 📚 Additional Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Postman Documentation: https://learning.postman.com/
- JPA/Hibernate Documentation: https://hibernate.org/orm/documentation/
