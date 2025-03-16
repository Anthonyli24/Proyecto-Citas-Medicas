package com.example.proyecto_sistema_citas.presentation.RegistroMedicos;

import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("RegistroMedicos")
public class Controller {
    @Autowired
    private Service service;


    @GetMapping("/registro/medicos")
    public String registrarMedicos(Model model) {
        model.addAttribute("medico", new Medico()); // Para enlazar con el formulario
        return "/presentation/RegistroMedicos/View";
    }



}
