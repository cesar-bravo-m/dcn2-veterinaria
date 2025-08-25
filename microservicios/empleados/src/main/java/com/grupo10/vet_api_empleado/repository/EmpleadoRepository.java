package com.grupo10.vet_api_empleado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupo10.vet_api_empleado.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    @Query(value = "select p.persona_id, p.rut, p.nombres , p.paterno, p.materno, c.nombre, p.paciente_flag, p.funcionario_flag \n" + //
                "from vt_persona p\n" + //
                "inner join vt_cargo c\n" + //
                "\ton p.cargo_id = c.cargo_id", nativeQuery = true)
    List<Empleado> findEmpleados();

}
