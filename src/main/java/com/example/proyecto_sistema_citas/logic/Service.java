package com.example.proyecto_sistema_citas.logic;

import com.example.proyecto_sistema_citas.data.MedicoRepository;
import com.example.proyecto_sistema_citas.data.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service("service")
public class Service {
   @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    public Iterable<Usuario> usuarioFindAll(){
       return usuarioRepository.findAll();
   }

   @Transactional
    public void registrarMedico(Medico medico){
       medicoRepository.save(medico);
   }

    public Iterable<Medico> medicoFindAll(){
         return medicoRepository.findAll();
    }



}
