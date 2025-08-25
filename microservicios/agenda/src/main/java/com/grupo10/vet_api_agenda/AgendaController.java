package com.grupo10.vet_api_agenda;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo10.vet_api_agenda.model.Agenda;
import com.grupo10.vet_api_agenda.repository.AgendaRepository;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    @Autowired
    private AgendaRepository agendaRepository;

    @GetMapping
    public List<Agenda> getAgenda() {
        return agendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Agenda getAgendaById(@PathVariable Long id) {
        return agendaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Agenda createAgenda(@RequestBody Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    @PutMapping("/{id}")
    public Agenda updateAgenda(@PathVariable Long id, @RequestBody Agenda agenda) {
        return agendaRepository.findById(id).map(a -> {
            a.setMascotaId(agenda.getMascotaId());
            a.setFecha(agenda.getFecha());
            a.setNuloFlag(agenda.getNuloFlag());
            a.setNuloFecha(agenda.getNuloFecha());
            a.setRegistroFecha(agenda.getRegistroFecha());
            a.setUsuarioId(agenda.getUsuarioId());
            return agendaRepository.save(a);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteAgenda(@PathVariable Long id) {
        agendaRepository.deleteById(id);
    }

}
