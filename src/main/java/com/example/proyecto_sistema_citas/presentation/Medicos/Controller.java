package com.example.proyecto_sistema_citas.presentation.Medicos;

import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@org.springframework.stereotype.Controller("medicos")
public class Controller {
    @Autowired
    private Service service;

/*
    @GetMapping("/listado/medicos")
    public String ListarMedicos(Model model) {
        model.addAttribute("medicos", service.medicoFindAll());
        return "/presentation/RegistroMedicos/ListadoMedicos";
    }
*/

/*
    @PostMapping("/filtrado/medicos")
    public String filtrarMedicos(@RequestParam(required = false) String especialidad,
                                @RequestParam(required = false) String localidad,
                                Model model) {
        model.addAttribute("medicos", service.FiltradoMedicos(especialidad, localidad));
        return "/presentation/home/home";
    }

*/
@PostMapping("/filtrado/medicos")
public String filtrarMedicos(@RequestParam(required = false) String especialidad,
                             @RequestParam(required = false) String localidad,
                             Model model) {
    List<Medico> medicosFiltrados = service.FiltradoMedicos(especialidad, localidad);

    model.addAttribute("medicos", medicosFiltrados);
    return "/presentation/home/home";
}
}
