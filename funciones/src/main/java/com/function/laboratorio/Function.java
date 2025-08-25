package com.function.laboratorio;

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
    
    @FunctionName("Laboratorio")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Procesando solicitud HTTP de Java para Laboratorio.");

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
        String mascotaIdParam = request.getQueryParameters().get("mascota_id");
        String examenParam = request.getQueryParameters().get("examen");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long laboratorioId = Long.parseLong(idParam);
                LaboratorioDTO laboratorio = databaseService.getLaboratorioById(laboratorioId);
                
                if (laboratorio != null) {
                    String jsonResponse = objectMapper.writeValueAsString(laboratorio);
                    return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(jsonResponse)
                        .build();
                } else {
                    return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("Registro de laboratorio no encontrado con ID: " + laboratorioId)
                        .build();
                }
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de laboratorio inválido")
                    .build();
            }
        } else if (mascotaIdParam != null && !mascotaIdParam.isEmpty()) {
            try {
                Long mascotaId = Long.parseLong(mascotaIdParam);
                List<LaboratorioDTO> laboratorios = databaseService.getLaboratoriosByMascotaId(mascotaId);
                context.getLogger().info("Recuperados " + laboratorios.size() + " registros de laboratorio de la mascota " + mascotaId);
                
                String jsonResponse = objectMapper.writeValueAsString(laboratorios);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } catch (NumberFormatException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Formato de ID de mascota inválido")
                    .build();
            }
        } else if (examenParam != null && !examenParam.isEmpty()) {
            List<LaboratorioDTO> laboratorios = databaseService.getLaboratoriosByExamen(examenParam);
            context.getLogger().info("Recuperados " + laboratorios.size() + " registros de laboratorio para el examen: " + examenParam);
            
            String jsonResponse = objectMapper.writeValueAsString(laboratorios);
            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
        } else {
            List<LaboratorioDTO> laboratorios = databaseService.getAllLaboratorios();
            context.getLogger().info("Recuperados " + laboratorios.size() + " registros de laboratorio de la base de datos");
            
            String jsonResponse = objectMapper.writeValueAsString(laboratorios);
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
            LaboratorioDTO laboratorio = objectMapper.readValue(body.get(), LaboratorioDTO.class);
            
            // Validate required fields
            if (laboratorio.getMascotaId() == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("mascota_id es requerido")
                    .build();
            }
            
            if (laboratorio.getExamen() == null || laboratorio.getExamen().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("examen es requerido")
                    .build();
            }
            
            if (laboratorio.getResultado() == null || laboratorio.getResultado().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("resultado es requerido")
                    .build();
            }
            
            LaboratorioDTO createdLaboratorio = databaseService.createLaboratorio(laboratorio);
            
            if (createdLaboratorio != null) {
                String jsonResponse = objectMapper.writeValueAsString(createdLaboratorio);
                return request.createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear registro de laboratorio")
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
            Long laboratorioId = Long.parseLong(idParam);
            Optional<String> body = request.getBody();
            
            if (!body.isPresent()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Se requiere cuerpo de solicitud")
                    .build();
            }
            
            LaboratorioDTO laboratorio = objectMapper.readValue(body.get(), LaboratorioDTO.class);
            laboratorio.setLaboratorioId(laboratorioId);
            
            // Validate required fields
            if (laboratorio.getMascotaId() == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("mascota_id es requerido")
                    .build();
            }
            
            if (laboratorio.getExamen() == null || laboratorio.getExamen().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("examen es requerido")
                    .build();
            }
            
            if (laboratorio.getResultado() == null || laboratorio.getResultado().trim().isEmpty()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("resultado es requerido")
                    .build();
            }
            
            boolean updated = databaseService.updateLaboratorio(laboratorio);
            
            if (updated) {
                LaboratorioDTO updatedLaboratorio = databaseService.getLaboratorioById(laboratorioId);
                String jsonResponse = objectMapper.writeValueAsString(updatedLaboratorio);
                return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonResponse)
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Registro de laboratorio no encontrado con ID: " + laboratorioId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de laboratorio inválido")
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
            Long laboratorioId = Long.parseLong(idParam);
            boolean deleted = databaseService.deleteLaboratorio(laboratorioId);
            
            if (deleted) {
                return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .body("Registro de laboratorio eliminado exitosamente")
                    .build();
            } else {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Registro de laboratorio no encontrado con ID: " + laboratorioId)
                    .build();
            }
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Formato de ID de laboratorio inválido")
                .build();
        }
    }
}
