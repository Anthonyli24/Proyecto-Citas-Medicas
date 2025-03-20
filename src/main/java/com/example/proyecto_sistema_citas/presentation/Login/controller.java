package com.example.proyecto_sistema_citas.presentation.Login;

import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import com.example.proyecto_sistema_citas.logic.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller("Login")
public class controller {
    @Autowired
    private Service service;

    @GetMapping("/about")
    public String About(Model model) {
        return "/presentation/About/about";
    }

    @GetMapping("/registro")
    public String Form(Model model) {

        Iterable<Rol> roles = service.rolFindAll();

        // Agregar un nuevo objeto Usuario al modelo para el formulario
        model.addAttribute("usuario", new Usuario());

        // Pasar la lista de roles al modelo
        model.addAttribute("roles", roles);
        return "/presentation/Registro/registro";
    }

    @GetMapping("/registroMedico")
    public String registroMedico(Model model) {
        return "/presentation/Registro/registroMedico";
    }

    @GetMapping("/login")
    public String Login(Model model) {
        return "/presentation/Login/login";
    }

    @GetMapping("/css")
    public String CSS(Model model) {
        return "/static/css";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized(Model model) {
        return "redirect:/presentation/Login/notAuthorized";
    }

    @GetMapping("/")
    public String redirigir() {
        return "redirect:/home";
    }

    @PostMapping("/register/guardar")
    public String RegistroUsuario(@ModelAttribute Usuario usuario, Model model) {
        System.out.println(usuario + usuario.getRol().getNombre());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encriptada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(encriptada);
        service.RegistrarUsuario(usuario);

        return "/presentation/Registro/RegistroExitoso";
    }

    @GetMapping("/RegistroExitoso")
    public String RegistroExitoso(Model model) {
        return "redirect: /";
    }
}