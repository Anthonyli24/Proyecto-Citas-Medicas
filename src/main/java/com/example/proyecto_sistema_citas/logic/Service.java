package com.example.proyecto_sistema_citas.logic;

import com.example.proyecto_sistema_citas.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service("service")
public class Service {
   @Autowired
    private UsuarioRepository usuarioRepository;


   public Iterable<Usuario> usuarioFindAll(){
       return usuarioRepository.findAll();
   }
}
