package com.grupo10.vet_api_agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo10.vet_api_agenda.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

}
