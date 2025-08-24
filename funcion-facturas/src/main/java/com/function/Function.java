package com.function;

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
    
    @FunctionName("Facturas")
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
        
        if (idParam == null || idParam.isEmpty()) {
            List<DocumentoDTO> documentos = databaseService.getAllDocumentos();
            context.getLogger().info("Recuperados " + documentos.size() + " documentos de la base de datos");
            
            String jsonResponse = objectMapper.writeValueAsString(documentos);
            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
        }
        
        try {
            Long documentoId = Long.parseLong(idParam);
            DocumentoDTO documento = databaseService.getDocumentoById(documentoId);
            
            if (documento != null) {
                String jsonResponse = objectMapper.writeValueAsString(documento);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Documento no encontrado con ID: " + documentoId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de documento inválido")
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
            DocumentoDTO documento = objectMapper.readValue(body.get(), DocumentoDTO.class);
            
            if (documento.getRegistroFecha() == null) {
                documento.setRegistroFecha(java.time.LocalDateTime.now());
            }
            
            DocumentoDTO createdDocumento = databaseService.createDocumento(documento);
            
            if (createdDocumento != null) {
                String jsonResponse = objectMapper.writeValueAsString(createdDocumento);
                return request.createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear documento")
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
                .body("PUT requiere ID de documento en parámetro de consulta: ?id={id}")
                .build();
        }
        
        try {
            Long documentoId = Long.parseLong(idParam);
            Optional<String> body = request.getBody();
            
            if (!body.isPresent()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Se requiere cuerpo de solicitud")
                    .build();
            }
            
            DocumentoDTO documento = objectMapper.readValue(body.get(), DocumentoDTO.class);
            documento.setDocumentoId(documentoId);
            
            boolean updated = databaseService.updateDocumento(documento);
            
            if (updated) {
                DocumentoDTO updatedDocumento = databaseService.getDocumentoById(documentoId);
                String jsonResponse = objectMapper.writeValueAsString(updatedDocumento);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Documento no encontrado con ID: " + documentoId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de documento inválido")
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
                .body("DELETE requiere ID de documento en parámetro de consulta: ?id={id}")
                .build();
        }
        
        try {
            Long documentoId = Long.parseLong(idParam);
            boolean deleted = databaseService.deleteDocumento(documentoId);
            
            if (deleted) {
                return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .body("Documento eliminado exitosamente")
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Documento no encontrado con ID: " + documentoId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de documento inválido")
                .build();
        }
    }
}
