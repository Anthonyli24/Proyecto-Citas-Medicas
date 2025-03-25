package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Cita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepository extends CrudRepository<Cita, String> {
    List<Cita> findByUsuarioId(String usuarioId);

    Iterable<Cita> findByStatusContainingAndMedicoUsuarioNombreContainingIgnoreCase(String status, String doctor);
}
