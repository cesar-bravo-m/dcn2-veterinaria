package com.function.clientes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClienteDTO {
    
    @JsonProperty("cliente_id")
    private Long clienteId;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("paterno")
    private String paterno;
    
    @JsonProperty("materno")
    private String materno;
    
    @JsonProperty("rut")
    private String rut;
    
    public ClienteDTO() {}
    
    public ClienteDTO(Long clienteId, String nombre, String paterno, String materno, String rut) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.rut = rut;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPaterno() {
        return paterno;
    }
    
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }
    
    public String getMaterno() {
        return materno;
    }
    
    public void setMaterno(String materno) {
        this.materno = materno;
    }
    
    public String getRut() {
        return rut;
    }
    
    public void setRut(String rut) {
        this.rut = rut;
    }
}
