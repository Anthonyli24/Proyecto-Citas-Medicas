package com.example.proyecto_sistema_citas.data;


import com.example.proyecto_sistema_citas.logic.Medico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MedicoRepository extends CrudRepository<Medico, String> {
    List<Medico> findByEspecialidadContainingIgnoreCaseAndLocalidadContainingIgnoreCase(
            String especialidad, String localidad);

}

