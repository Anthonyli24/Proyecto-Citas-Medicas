package com.example.proyecto_sistema_citas.presentation.Horario;

import java.time.LocalTime;
import com.example.proyecto_sistema_citas.logic.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@org.springframework.stereotype.Controller("Horario")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/agregar")
    public String mostrarFormulario() {
        return "horario/agregar";
    }

    @PostMapping("/horario/agregar")
    public String agregarHorario(@RequestParam String medicoId,
                                 @RequestParam String dia,
                                 @RequestParam String horaInicio,
                                 @RequestParam String horaFin,
                                 RedirectAttributes redirectAttributes) {
        try {
            service.agregarHorario(medicoId, dia, LocalTime.parse(horaInicio), LocalTime.parse(horaFin));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/MiPerfil?id=" + medicoId;
    }

    @GetMapping("/eliminar")
    public String Eliminar() {
        return "horario/eliminar";
    }

    @PostMapping("/horario/eliminar")
    public String eliminarHorario(@RequestParam String id, @RequestParam String dia) {
        service.eliminarHorario(id, dia);
        return "redirect:/MiPerfil?id=" + id;
    }
}