package com.grupo10.vet_api_agenda.service;

import java.util.List;

import com.grupo10.vet_api_agenda.model.Agenda;

public interface AgendaService {

    List<Agenda> findAll();
    Agenda save(Agenda agenda);

}
