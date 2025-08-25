package com.grupo10.vet_api_inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo10.vet_api_inventario.model.Inventario;
import com.grupo10.vet_api_inventario.repository.InventarioRepository;

public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }    

    @Override
    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }


}
