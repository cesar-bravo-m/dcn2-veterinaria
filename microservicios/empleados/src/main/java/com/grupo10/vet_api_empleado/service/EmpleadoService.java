package com.grupo10.vet_api_empleado.service;

import java.util.List;

import com.grupo10.vet_api_empleado.model.Empleado;

public interface EmpleadoService {

    List<Empleado> findEmpleados();
    Empleado save(Empleado empleado);

}
