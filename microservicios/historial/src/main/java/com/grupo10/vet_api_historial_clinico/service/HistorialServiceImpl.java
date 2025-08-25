package com.grupo10.vet_api_historial_clinico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo10.vet_api_historial_clinico.model.Historial;
import com.grupo10.vet_api_historial_clinico.repository.HistorialRepository;

public class HistorialServiceImpl implements HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    @Override
    public List<Historial> findAll() {
        return historialRepository.findAll();
    }    

    @Override
    public Historial save(Historial inventario) {
        return historialRepository.save(inventario);
    }

}
