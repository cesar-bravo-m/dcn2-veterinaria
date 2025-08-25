package com.grupo10.vet_api_agenda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "vt_agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agenda_seq")
    @SequenceGenerator(name = "agenda_seq", sequenceName = "vt_agenda_agenda_id_seq", allocationSize = 1)
    @Column(name = "agenda_id")
    private Long agendaId;
    @Column(name = "mascota_id")
    private Long mascotaId;
    @Column(name = "fecha")
    private Long fecha;
    @Column(name = "nulo_flag")
    private Long nuloFlag;
    @Column(name = "nulo_fecha")
    private Long nuloFecha;
    @Column(name = "registro_fecha")
    private Long registroFecha;
    @Column(name = "usuario_id")
    private Long usuarioId;

    public Long getAgendaId() {
        return agendaId;
    }
    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }
    public Long getMascotaId() {
        return mascotaId;
    }
    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }
    public Long getFecha() {
        return fecha;
    }
    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }
    public Long getNuloFlag() {
        return nuloFlag;
    }
    public void setNuloFlag(Long nuloFlag) {
        this.nuloFlag = nuloFlag;
    }
    public Long getNuloFecha() {
        return nuloFecha;
    }
    public void setNuloFecha(Long nuloFecha) {
        this.nuloFecha = nuloFecha;
    }
    public Long getRegistroFecha() {
        return registroFecha;
    }
    public void setRegistroFecha(Long registroFecha) {
        this.registroFecha = registroFecha;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

}
