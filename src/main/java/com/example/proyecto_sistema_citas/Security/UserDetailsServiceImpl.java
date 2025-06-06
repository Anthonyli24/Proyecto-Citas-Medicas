package com.example.proyecto_sistema_citas.Security;

import org.springframework.stereotype.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.proyecto_sistema_citas.data.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Usuario usuario = usuarioRepository.findById(username).get();
            return new UserDetailsImp(usuario);

        }catch(Exception e){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}