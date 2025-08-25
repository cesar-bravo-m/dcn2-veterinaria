package com.grupo10.vet_api_inventario;

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

import com.grupo10.vet_api_inventario.model.Inventario;
import com.grupo10.vet_api_inventario.repository.InventarioRepository;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @GetMapping
    public List<Inventario> getInventario() {
        return inventarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Inventario getInventarioById(@PathVariable Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Inventario createInventario(@RequestBody Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @PutMapping("/{id}")
    public Inventario updateInventario(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioRepository.findById(id).map(i -> {
            i.setInventarioTipoId(inventario.getInventarioTipoId());
            i.setNombre(inventario.getNombre());
            i.setMedidaUnidadId(inventario.getMedidaUnidadId());
            i.setStock(inventario.getStock());
            i.setUsuarioId(inventario.getUsuarioId());
            return inventarioRepository.save(i);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteInventario(@PathVariable Long id) {
        inventarioRepository.deleteById(id);
    }

}
