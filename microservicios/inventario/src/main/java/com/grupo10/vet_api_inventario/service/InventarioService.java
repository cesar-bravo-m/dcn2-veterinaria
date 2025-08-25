package com.grupo10.vet_api_inventario.service;

import java.util.List;

import com.grupo10.vet_api_inventario.model.Inventario;

public interface InventarioService {

    List<Inventario> findAll();
    Inventario save(Inventario inventario);
}
