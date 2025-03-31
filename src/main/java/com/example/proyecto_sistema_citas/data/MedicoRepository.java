package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends CrudRepository<Medico, String> {
    List<Medico> findByEspecialidadContainingIgnoreCaseAndLocalidadContainingIgnoreCase(String especialidad, String localidad);

    List<Medico> findByStatusContainingIgnoreCase(String status);

    Medico findByUsuarioNombreContainingIgnoreCase(String doctor);

}