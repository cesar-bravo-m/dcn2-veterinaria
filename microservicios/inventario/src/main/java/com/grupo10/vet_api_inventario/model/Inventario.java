package com.grupo10.vet_api_inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventario_seq")
    @SequenceGenerator(name = "inventario_seq", sequenceName = "inventario_inventario_id_seq", allocationSize = 1)
    @Column(name = "inventario_id")
    private Long inventarioId;
    @Column(name = "inventario_tipo_id")
    private Long inventarioTipoId;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "medida_unidad_id")
    private Long medidaUnidadId;
    @Column(name = "stock")
    private Long stock;
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    public Long getInventarioId() {
        return inventarioId;
    }
    public void setInventarioId(Long inventarioId) {
        this.inventarioId = inventarioId;
    }
    public Long getInventarioTipoId() {
        return inventarioTipoId;
    }
    public void setInventarioTipoId(Long inventarioTipoId) {
        this.inventarioTipoId = inventarioTipoId;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Long getMedidaUnidadId() {
        return medidaUnidadId;
    }
    public void setMedidaUnidadId(Long medidaUnidadId) {
        this.medidaUnidadId = medidaUnidadId;
    }
    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
}
