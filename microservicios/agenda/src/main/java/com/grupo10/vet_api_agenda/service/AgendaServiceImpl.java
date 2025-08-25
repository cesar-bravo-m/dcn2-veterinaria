package com.grupo10.vet_api_agenda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo10.vet_api_agenda.model.Agenda;
import com.grupo10.vet_api_agenda.repository.AgendaRepository;

public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Override
    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }    

    @Override
    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

}
