package com.function.laboratorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LaboratorioDTO {
    
    @JsonProperty("laboratorio_id")
    private Long laboratorioId;
    
    @JsonProperty("mascota_id")
    private Long mascotaId;
    
    @JsonProperty("fecha")
    private LocalDateTime fecha;
    
    @JsonProperty("examen")
    private String examen;
    
    @JsonProperty("resultado")
    private String resultado;
    
    @JsonProperty("valor")
    private BigDecimal valor;
    
    @JsonProperty("unidad")
    private String unidad;
    
    public LaboratorioDTO() {}
    
    public LaboratorioDTO(Long laboratorioId, Long mascotaId, LocalDateTime fecha, 
                         String examen, String resultado, BigDecimal valor, String unidad) {
        this.laboratorioId = laboratorioId;
        this.mascotaId = mascotaId;
        this.fecha = fecha;
        this.examen = examen;
        this.resultado = resultado;
        this.valor = valor;
        this.unidad = unidad;
    }
    
    public Long getLaboratorioId() {
        return laboratorioId;
    }
    
    public void setLaboratorioId(Long laboratorioId) {
        this.laboratorioId = laboratorioId;
    }
    
    public Long getMascotaId() {
        return mascotaId;
    }
    
    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getExamen() {
        return examen;
    }
    
    public void setExamen(String examen) {
        this.examen = examen;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getUnidad() {
        return unidad;
    }
    
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    
    @Override
    public String toString() {
        return "LaboratorioDTO{" +
                "laboratorioId=" + laboratorioId +
                ", mascotaId=" + mascotaId +
                ", fecha=" + fecha +
                ", examen='" + examen + '\'' +
                ", resultado='" + resultado + '\'' +
                ", valor=" + valor +
                ", unidad='" + unidad + '\'' +
                '}';
    }
}
