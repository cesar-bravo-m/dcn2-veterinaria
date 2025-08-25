package com.grupo10.vet_api_empleado.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo10.vet_api_empleado.model.Empleado;
import com.grupo10.vet_api_empleado.repository.EmpleadoRepository;

public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> findEmpleados() {
        return empleadoRepository.findEmpleados();
    }

    @Override
    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

}
