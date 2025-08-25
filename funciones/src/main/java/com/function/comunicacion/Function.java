package com.function.comunicacion;

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
    
    private final ObjectMapper objectMapper;
    private final DatabaseService databaseService;
    
    public Function() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.databaseService = new DatabaseService();
    }
    
    @FunctionName("Comunicacion")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Procesando solicitud HTTP de Java para Comunicacion.");

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
        String estadoParam = request.getQueryParameters().get("estado");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long mensajeId = Long.parseLong(idParam);
                MensajeDTO mensaje = databaseService.getMensajeById(mensajeId);
                
                if (mensaje != null) {
                    String jsonResponse = objectMapper.writeValueAsString(mensaje);
                    return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(jsonResponse)
                        .build();
                } else {
                    return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("Mensaje no encontrado con ID: " + mensajeId)
                        .build();
                }
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de mensaje inválido")
                    .build();
            }
        } else if (clienteIdParam != null && !clienteIdParam.isEmpty()) {
            try {
                Long clienteId = Long.parseLong(clienteIdParam);
                List<MensajeDTO> mensajes = databaseService.getMensajesByClienteId(clienteId);
                context.getLogger().info("Recuperados " + mensajes.size() + " mensajes del cliente " + clienteId);
                
                String jsonResponse = objectMapper.writeValueAsString(mensajes);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de cliente inválido")
                    .build();
            }
        } else if (estadoParam != null && !estadoParam.isEmpty()) {
            List<MensajeDTO> mensajes = databaseService.getMensajesByEstado(estadoParam);
            context.getLogger().info("Recuperados " + mensajes.size() + " mensajes con estado: " + estadoParam);
            
            String jsonResponse = objectMapper.writeValueAsString(mensajes);
            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
        } else {
            List<MensajeDTO> mensajes = databaseService.getAllMensajes();
            context.getLogger().info("Recuperados " + mensajes.size() + " mensajes de la base de datos");
            
            String jsonResponse = objectMapper.writeValueAsString(mensajes);
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
            MensajeDTO mensaje = objectMapper.readValue(body.get(), MensajeDTO.class);
            
            // Validate required fields
            if (mensaje.getClienteId() == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("cliente_id es requerido")
                    .build();
            }
            
            if (mensaje.getContenido() == null || mensaje.getContenido().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("contenido es requerido")
                    .build();
            }
            
            MensajeDTO createdMensaje = databaseService.createMensaje(mensaje);
            
            if (createdMensaje != null) {
                String jsonResponse = objectMapper.writeValueAsString(createdMensaje);
                return request.createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear mensaje")
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
            Long mensajeId = Long.parseLong(idParam);
            Optional<String> body = request.getBody();
            
            if (!body.isPresent()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Se requiere cuerpo de solicitud")
                    .build();
            }
            
            MensajeDTO mensaje = objectMapper.readValue(body.get(), MensajeDTO.class);
            mensaje.setMensajeId(mensajeId);
            
            // Validate required fields
            if (mensaje.getClienteId() == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("cliente_id es requerido")
                    .build();
            }
            
            if (mensaje.getContenido() == null || mensaje.getContenido().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("contenido es requerido")
                    .build();
            }
            
            if (mensaje.getEstado() == null || mensaje.getEstado().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("estado es requerido")
                    .build();
            }
            
            boolean updated = databaseService.updateMensaje(mensaje);
            
            if (updated) {
                MensajeDTO updatedMensaje = databaseService.getMensajeById(mensajeId);
                String jsonResponse = objectMapper.writeValueAsString(updatedMensaje);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Mensaje no encontrado con ID: " + mensajeId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de mensaje inválido")
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
            Long mensajeId = Long.parseLong(idParam);
            boolean deleted = databaseService.deleteMensaje(mensajeId);
            
            if (deleted) {
                return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .body("Mensaje eliminado exitosamente")
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Mensaje no encontrado con ID: " + mensajeId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de mensaje inválido")
                .build();
        }
    }
}
