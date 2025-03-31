package com.example.proyecto_sistema_citas.data;

import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario,String> {
    Usuario findByNombreContainingIgnoreCase(String paciente);

}