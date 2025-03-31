package com.example.proyecto_sistema_citas.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.proyecto_sistema_citas.logic.Horario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HorarioRepository extends CrudRepository<Horario, String> {
    List<Horario> findByMedicoId(@Param("medicoId") String medicoId);

    List<Horario> findByMedicoIdAndDia(String medicoId, String dia);

    @Query("SELECT h FROM Horario h WHERE h.medico.id = :medicoId")
    List<Horario> obtenerHorariosPorMedico(@Param("medicoId") String medicoId);
}