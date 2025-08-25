package com.function.comunicacion;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MensajeDTO {
    
    @JsonProperty("mensaje_id")
    private Long mensajeId;
    
    @JsonProperty("cliente_id")
    private Long clienteId;
    
    @JsonProperty("fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @JsonProperty("contenido")
    private String contenido;
    
    @JsonProperty("estado")
    private String estado;
    
    public MensajeDTO() {}
    
    public MensajeDTO(Long mensajeId, Long clienteId, LocalDateTime fechaEnvio, 
                     String contenido, String estado) {
        this.mensajeId = mensajeId;
        this.clienteId = clienteId;
        this.fechaEnvio = fechaEnvio;
        this.contenido = contenido;
        this.estado = estado;
    }
    
    public Long getMensajeId() {
        return mensajeId;
    }
    
    public void setMensajeId(Long mensajeId) {
        this.mensajeId = mensajeId;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "MensajeDTO{" +
                "mensajeId=" + mensajeId +
                ", clienteId=" + clienteId +
                ", fechaEnvio=" + fechaEnvio +
                ", contenido='" + contenido + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
