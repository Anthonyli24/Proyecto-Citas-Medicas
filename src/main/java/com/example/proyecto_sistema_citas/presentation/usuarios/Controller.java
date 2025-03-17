package com.example.proyecto_sistema_citas.presentation.usuarios;

import com.example.proyecto_sistema_citas.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("usuarios")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/login")
    public String Login(Model model) {
        return "/presentation/Login/login";
    }

    @GetMapping("/listado/usuarios")
    public String ListarUsuarios(Model model) {
        model.addAttribute("usuarios", service.usuarioFindAll());
        return "/presentation/usuarios/View";
    }

    //ESTO VA EN OTRO LADO
    @GetMapping("/about")
    public String About(Model model) {
        return "/presentation/about/about";
    }

}
