package com.grupo10.vet_api_empleado.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "vt_persona")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "vt_persona_persona_id_seq", allocationSize = 1)
    @Column(name = "persona_id")
    private Long personaId;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "paterno")
    private String paterno;
    @Column(name = "materno")
    private String materno;
    @Column(name = "rut")
    private String rut;
    @Column(name = "cargo_id")
    private Long cargoId;
    @Column(name = "pacienteFlag")
    private Boolean pacienteFlag;
    @Column(name = "funcionario_flag")
    private Boolean functionarioFlag;
    @Column(name = "registro_fecha")
    private LocalDateTime registroFecha;

    public Long getPersonaId() {
        return personaId;
    }
    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
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
    public Long getCargoId() {
        return cargoId;
    }
    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }
    public Boolean getPacienteFlag() {
        return pacienteFlag;
    }
    public void setPacienteFlag(Boolean pacienteFlag) {
        this.pacienteFlag = pacienteFlag;
    }
    public Boolean getFunctionarioFlag() {
        return functionarioFlag;
    }
    public void setFunctionarioFlag(Boolean functionarioFlag) {
        this.functionarioFlag = functionarioFlag;
    }
    public LocalDateTime getRegistroFecha() {
        return registroFecha;
    }
    public void setRegistroFecha(LocalDateTime registroFecha) {
        this.registroFecha = registroFecha;
    }

    
}
