package com.example.proyecto_sistema_citas.presentation.Usuarios;

import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("usuarios")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/home")
    public String ListarUsuarios(Model model) {
        model.addAttribute("medicos", service.medicoFindAll());
        return "/presentation/Home/home";
    }

}
