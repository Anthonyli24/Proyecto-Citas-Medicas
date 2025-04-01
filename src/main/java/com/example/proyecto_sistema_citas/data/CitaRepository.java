package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Cita;
import com.example.proyecto_sistema_citas.logic.Medico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends CrudRepository<Cita, String> {
    List<Cita> findByUsuarioId(String usuarioId);

    Iterable<Cita> findByStatusContainingAndMedicoUsuarioNombreContainingIgnoreCase(String status, String doctor);

    Iterable<Cita> findByMedicoId(String id);

    Iterable<Cita> findByStatusAndMedicoId(String status, String id);

    Iterable<Cita> findByMedicoUsuarioNombreContainingIgnoreCaseAndMedicoId(String doctor, String id);

    Iterable<Cita> findByStatusAndUsuarioId(String status, String id);

    Iterable<Cita> findByUsuarioNombreContainingIgnoreCaseAndUsuarioId(String paciente, String id);

    Iterable<Cita> findByStatus(String status);

    @Query("SELECT c.hora FROM Cita c WHERE c.medico = :medico AND c.fecha = :fecha")
    List<LocalTime> findOcupadosByMedicoAndFecha(@Param("medico") Medico medico, @Param("fecha") LocalDate fecha);
}