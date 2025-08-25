package com.grupo10.vet_api_historial_clinico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo10.vet_api_historial_clinico.model.Historial;

public interface HistorialRepository extends JpaRepository<Historial, Long> {

}
