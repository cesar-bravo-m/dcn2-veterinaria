package com.function.mascotas;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class Function {
    
    private final DatabaseService databaseService;
    private final ObjectMapper objectMapper;
    
    public Function() {
        this.databaseService = new DatabaseService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @FunctionName("Mascotas")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Procesando solicitud HTTP de Java.");

        try {
            if (!databaseService.testConnection()) {
                context.getLogger().severe("Error al conectar con la base de datos");
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error de conexión con la base de datos")
                    .build();
            }
            
            String method = request.getHttpMethod().name();
            context.getLogger().info("Método HTTP: " + method);
            
            switch (method) {
                case "GET":
                    return handleGet(request, context);
                case "POST":
                    return handlePost(request, context);
                case "PUT":
                    return handlePut(request, context);
                case "DELETE":
                    return handleDelete(request, context);
                default:
                    return request.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED)
                        .body("Método no permitido")
                        .build();
            }
                
        } catch (Exception e) {
            context.getLogger().severe("Error procesando solicitud: " + e.getMessage());
            e.printStackTrace();
            
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage())
                .build();
        }
    }
    
    private HttpResponseMessage handleGet(HttpRequestMessage<Optional<String>> request, 
                                        ExecutionContext context) throws Exception {
        String idParam = request.getQueryParameters().get("id");
        String clienteIdParam = request.getQueryParameters().get("cliente_id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long mascotaId = Long.parseLong(idParam);
                MascotaDTO mascota = databaseService.getMascotaById(mascotaId);
                
                if (mascota != null) {
                    String jsonResponse = objectMapper.writeValueAsString(mascota);
                    return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(jsonResponse)
                        .build();
                } else {
                    return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("Mascota no encontrada con ID: " + mascotaId)
                        .build();
                }
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de mascota inválido")
                    .build();
            }
        } else if (clienteIdParam != null && !clienteIdParam.isEmpty()) {
            try {
                Long clienteId = Long.parseLong(clienteIdParam);
                List<MascotaDTO> mascotas = databaseService.getMascotasByClienteId(clienteId);
                context.getLogger().info("Recuperadas " + mascotas.size() + " mascotas del cliente " + clienteId);
                
                String jsonResponse = objectMapper.writeValueAsString(mascotas);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de cliente inválido")
                    .build();
            }
        } else {
            List<MascotaDTO> mascotas = databaseService.getAllMascotas();
            context.getLogger().info("Recuperadas " + mascotas.size() + " mascotas de la base de datos");
            
            String jsonResponse = objectMapper.writeValueAsString(mascotas);
            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
        }
    }
    
    private HttpResponseMessage handlePost(HttpRequestMessage<Optional<String>> request, 
                                         ExecutionContext context) throws Exception {
        Optional<String> body = request.getBody();
        if (!body.isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Se requiere cuerpo de solicitud")
                .build();
        }
        
        try {
            MascotaDTO mascota = objectMapper.readValue(body.get(), MascotaDTO.class);
            
            // Set default value for domestico if not provided
            if (mascota.getDomestico() == null) {
                mascota.setDomestico(true);
            }
            
            MascotaDTO createdMascota = databaseService.createMascota(mascota);
            
            if (createdMascota != null) {
                String jsonResponse = objectMapper.writeValueAsString(createdMascota);
                return request.createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear mascota")
                    .build();
            }
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato JSON inválido: " + e.getMessage())
                .build();
        }
    }
    
    private HttpResponseMessage handlePut(HttpRequestMessage<Optional<String>> request, 
                                        ExecutionContext context) throws Exception {
        String idParam = request.getQueryParameters().get("id");
        
        if (idParam == null || idParam.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("PUT requiere ID en parámetro de consulta: ?id={id}")
                .build();
        }
        
        try {
            Long mascotaId = Long.parseLong(idParam);
            Optional<String> body = request.getBody();
            
            if (!body.isPresent()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Se requiere cuerpo de solicitud")
                    .build();
            }
            
            MascotaDTO mascota = objectMapper.readValue(body.get(), MascotaDTO.class);
            mascota.setMascotaId(mascotaId);
            
            boolean updated = databaseService.updateMascota(mascota);
            
            if (updated) {
                MascotaDTO updatedMascota = databaseService.getMascotaById(mascotaId);
                String jsonResponse = objectMapper.writeValueAsString(updatedMascota);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Mascota no encontrada con ID: " + mascotaId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de mascota inválido")
                .build();
        } catch (Exception e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato JSON inválido: " + e.getMessage())
                .build();
        }
    }
    
    private HttpResponseMessage handleDelete(HttpRequestMessage<Optional<String>> request, 
                                           ExecutionContext context) throws Exception {
        String idParam = request.getQueryParameters().get("id");
        
        if (idParam == null || idParam.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("DELETE requiere ID en parámetro de consulta: ?id={id}")
                .build();
        }
        
        try {
            Long mascotaId = Long.parseLong(idParam);
            boolean deleted = databaseService.deleteMascota(mascotaId);
            
            if (deleted) {
                return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .body("Mascota eliminada exitosamente")
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Mascota no encontrada con ID: " + mascotaId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de mascota inválido")
                .build();
        }
    }
}
