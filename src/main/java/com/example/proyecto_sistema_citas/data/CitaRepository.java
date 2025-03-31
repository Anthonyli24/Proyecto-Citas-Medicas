package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Cita;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CitaRepository extends CrudRepository<Cita, String> {
    List<Cita> findByUsuarioId(String usuarioId);

    Iterable<Cita> findByStatusContainingAndMedicoUsuarioNombreContainingIgnoreCase(String status, String doctor);

    Iterable<Cita> findByMedicoId(String id);

    Iterable<Cita> findByStatusAndMedicoId(String status, String id);

    Iterable<Cita> findByMedicoUsuarioNombreContainingIgnoreCaseAndMedicoId(String doctor, String id);

    Iterable<Cita> findByStatusAndUsuarioId(String status, String id);

    Iterable<Cita> findByUsuarioNombreContainingIgnoreCaseAndUsuarioId(String paciente, String id);
}