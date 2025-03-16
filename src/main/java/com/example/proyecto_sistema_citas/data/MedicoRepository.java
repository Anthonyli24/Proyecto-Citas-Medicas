package com.example.proyecto_sistema_citas.data;


import com.example.proyecto_sistema_citas.logic.Medico;
import org.springframework.data.repository.CrudRepository;

public interface MedicoRepository extends CrudRepository<Medico,String> {
}

