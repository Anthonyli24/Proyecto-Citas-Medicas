package com.example.proyecto_sistema_citas.logic;

import com.example.proyecto_sistema_citas.data.MedicoRepository;
import com.example.proyecto_sistema_citas.data.RolRepository;
import com.example.proyecto_sistema_citas.data.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@org.springframework.stereotype.Service("service")
public class Service {
   @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private RolRepository rolRepository;

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

    @Transactional
    public void RegistrarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public Iterable<Rol> rolFindAll(){
        return rolRepository.findAll();
    }

    public Usuario findUsuarioById(String id){
        return usuarioRepository.findById(id).get() ;
    }

    public boolean existeUsuarioPorId(String id){
        return usuarioRepository.existsById(id);
    }



    public List<Medico> FiltradoMedicos(String especialidad, String localidad) {
        if ((especialidad == null || especialidad.isEmpty()) && (localidad == null || localidad.isEmpty())) {
            return (List<Medico>) medicoRepository.findAll(); // Retorna todos si no hay filtros
        }

        if (especialidad == null) especialidad = "";
        if (localidad == null) localidad = "";

        return medicoRepository.findByEspecialidadContainingIgnoreCaseAndLocalidadContainingIgnoreCase(especialidad, localidad);
    }






}
