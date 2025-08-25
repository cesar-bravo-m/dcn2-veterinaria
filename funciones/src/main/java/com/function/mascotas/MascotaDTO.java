package com.function.mascotas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MascotaDTO {
    
    @JsonProperty("mascota_id")
    private Long mascotaId;
    
    @JsonProperty("cliente_id")
    private Long clienteId;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("especie")
    private String especie;
    
    @JsonProperty("edad")
    private Integer edad;
    
    @JsonProperty("domestico")
    private Boolean domestico;
    
    public MascotaDTO() {}
    
    public MascotaDTO(Long mascotaId, Long clienteId, String nombre, String especie, Integer edad, Boolean domestico) {
        this.mascotaId = mascotaId;
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.domestico = domestico;
    }
    
    public Long getMascotaId() {
        return mascotaId;
    }
    
    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
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
    
    public String getEspecie() {
        return especie;
    }
    
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public Boolean getDomestico() {
        return domestico;
    }
    
    public void setDomestico(Boolean domestico) {
        this.domestico = domestico;
    }
    
    @Override
    public String toString() {
        return "MascotaDTO{" +
                "mascotaId=" + mascotaId +
                ", clienteId=" + clienteId +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", domestico=" + domestico +
                '}';
    }
}
