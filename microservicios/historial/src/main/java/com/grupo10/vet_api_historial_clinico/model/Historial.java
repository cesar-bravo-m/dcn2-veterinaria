package com.grupo10.vet_api_historial_clinico.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "vt_historial")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_seq")
    @SequenceGenerator(name = "historial_seq", sequenceName = "vt_historial_historial_id_seq", allocationSize = 1)
    @Column(name = "historial_id")
    private Long historialId;
    @Column(name = "mascota_id")
    private Long mascotaId;
    @Column(name = "inventario_id")
    private Long inventarioId;
    @Column(name = "cantidad")
    private Long cantidad;
    @Column(name = "dosis_cantidad")
    private Long dosisCantidad;
    @Column(name = "registro_fecha")
    private Date registroFecha;

    public Long getHistorialId() {
        return historialId;
    }
    public void setHistorialId(Long historialId) {
        this.historialId = historialId;
    }
    public Long getMascotaId() {
        return mascotaId;
    }
    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }
    public Long getInventarioId() {
        return inventarioId;
    }
    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
    }
    public Long getCantidad() {
        return cantidad;
    }
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
    public Long getDosisCantidad() {
        return dosisCantidad;
    }
    public void setDosisCantidad(Long dosisCantidad) {
        this.dosisCantidad = dosisCantidad;
    }
    public Date getRegistroFecha() {
        return registroFecha;
    }
    public void setRegistroFecha(Date registroFecha) {
        this.registroFecha = registroFecha;
    }

    
}
