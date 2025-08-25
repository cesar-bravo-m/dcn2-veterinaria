package com.function.facturas;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentoDTO {
    
    @JsonProperty("documento_id")
    private Long documentoId;
    
    @JsonProperty("documento_tipo_id")
    private Long documentoTipoId;
    
    @JsonProperty("impuesto_id")
    private Long impuestoId;
    
    @JsonProperty("documento_numero")
    private Long documentoNumero;
    
    @JsonProperty("documento_fecha")
    private LocalDateTime documentoFecha;
    
    @JsonProperty("registro_fecha")
    private LocalDateTime registroFecha;
    
    @JsonProperty("usuario_id")
    private Long usuarioId;
    
    public DocumentoDTO() {}
    
    public DocumentoDTO(Long documentoId, Long documentoTipoId, Long impuestoId, 
                       Long documentoNumero, LocalDateTime documentoFecha, 
                       LocalDateTime registroFecha, Long usuarioId) {
        this.documentoId = documentoId;
        this.documentoTipoId = documentoTipoId;
        this.impuestoId = impuestoId;
        this.documentoNumero = documentoNumero;
        this.documentoFecha = documentoFecha;
        this.registroFecha = registroFecha;
        this.usuarioId = usuarioId;
    }
    
    public Long getDocumentoId() {
        return documentoId;
    }
    
    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }
    
    public Long getDocumentoTipoId() {
        return documentoTipoId;
    }
    
    public void setDocumentoTipoId(Long documentoTipoId) {
        this.documentoTipoId = documentoTipoId;
    }
    
    public Long getImpuestoId() {
        return impuestoId;
    }
    
    public void setImpuestoId(Long impuestoId) {
        this.impuestoId = impuestoId;
    }
    
    public Long getDocumentoNumero() {
        return documentoNumero;
    }
    
    public void setDocumentoNumero(Long documentoNumero) {
        this.documentoNumero = documentoNumero;
    }
    
    public LocalDateTime getDocumentoFecha() {
        return documentoFecha;
    }
    
    public void setDocumentoFecha(LocalDateTime documentoFecha) {
        this.documentoFecha = documentoFecha;
    }
    
    public LocalDateTime getRegistroFecha() {
        return registroFecha;
    }
    
    public void setRegistroFecha(LocalDateTime registroFecha) {
        this.registroFecha = registroFecha;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    @Override
    public String toString() {
        return "DocumentoDTO{" +
                "documentoId=" + documentoId +
                ", documentoTipoId=" + documentoTipoId +
                ", impuestoId=" + impuestoId +
                ", documentoNumero=" + documentoNumero +
                ", documentoFecha=" + documentoFecha +
                ", registroFecha=" + registroFecha +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
