package com.grupo10.vet_api_historial_clinico.service;

import java.util.List;

import com.grupo10.vet_api_historial_clinico.model.Historial;

public interface HistorialService {

    List<Historial> findAll();
    Historial save(Historial historial);

}
