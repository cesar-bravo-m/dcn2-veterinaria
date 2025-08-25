package com.grupo10.vet_api_empleado;

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

import com.grupo10.vet_api_empleado.model.Empleado;
import com.grupo10.vet_api_empleado.repository.EmpleadoRepository;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {

        @Autowired
    private EmpleadoRepository empleadoRepository;

    @GetMapping
    public List<Empleado> getEmpleado() {
        return empleadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Empleado getEmpleadoById(@PathVariable Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Empleado createEmpleado(@RequestBody Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @PutMapping("/{id}")
    public Empleado updateEmpleado(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoRepository.findById(id).map(a -> {
            a.setNombres(empleado.getNombres());
            a.setPaterno(empleado.getPaterno());
            a.setMaterno(empleado.getMaterno());
            a.setRut(empleado.getRut());
            a.setCargoId(empleado.getCargoId());
            a.setPacienteFlag(empleado.getPacienteFlag());
            a.setFunctionarioFlag(empleado.getFunctionarioFlag());
            a.setRegistroFecha(empleado.getRegistroFecha());
            return empleadoRepository.save(a);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteEmpleado(@PathVariable Long id) {
        empleadoRepository.deleteById(id);
    }

}
