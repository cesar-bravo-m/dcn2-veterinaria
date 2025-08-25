package com.grupo10.vet_api_historial_clinico;

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

import com.grupo10.vet_api_historial_clinico.model.Historial;
import com.grupo10.vet_api_historial_clinico.repository.HistorialRepository;

@RestController
@RequestMapping("/api/historial")

public class HistorialController {

    @Autowired
    private HistorialRepository historialRepository;

    @GetMapping
    public List<Historial> getHistorial() {
        return historialRepository.findAll();
    }

    @GetMapping("/{id}")
    public Historial getHistorialById(@PathVariable Long id) {
        return historialRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Historial createHistorial(@RequestBody Historial historial) {
        return historialRepository.save(historial);
    }

    @PutMapping("/{id}")
    public Historial updateHistorial(@PathVariable Long id, @RequestBody Historial historial) {
        return historialRepository.findById(id).map(h -> {
            h.setMascotaId(historial.getMascotaId());
            h.setHistorialId(historial.getHistorialId());
            h.setCantidad(historial.getCantidad());
            h.setDosisCantidad(historial.getDosisCantidad());
            return historialRepository.save(h);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteHistorial(@PathVariable Long id) {
        historialRepository.deleteById(id);
    }


}

