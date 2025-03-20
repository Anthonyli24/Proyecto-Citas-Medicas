package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Rol;
import org.springframework.data.repository.CrudRepository;

public interface RolRepository extends CrudRepository<Rol,String> {
}
