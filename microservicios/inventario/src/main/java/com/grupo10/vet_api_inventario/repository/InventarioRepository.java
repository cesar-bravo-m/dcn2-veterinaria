package com.grupo10.vet_api_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo10.vet_api_inventario.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

}
