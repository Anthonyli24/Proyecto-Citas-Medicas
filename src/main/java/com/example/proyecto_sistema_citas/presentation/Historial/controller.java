package com.example.proyecto_sistema_citas.presentation.Historial;


import com.example.proyecto_sistema_citas.logic.Cita;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("historial")
public class controller {

    @Autowired
    private Service service;


    @GetMapping("/historial")
    public String Historial(Model model, @AuthenticationPrincipal(expression = "usuario") Usuario usuario) {
        model.addAttribute("CitasMedico", service.obtenerCitasPorMedico(usuario.getId()));
        model.addAttribute("usuarioLog", usuario);
        return "/presentation/Cita/historial";
    }

    @GetMapping("/HistorialPaciente")
    public String historialCitasPacientes(Model model,@AuthenticationPrincipal(expression = "usuario") Usuario usuario){
        model.addAttribute("CitasPaciente",service.obtenerCitasPorUsuario(usuario.getId()));

        return "/presentation/Cita/HistorialPaciente";
    }


    @GetMapping("/MostrarNotas/{id}")
    public String MostrarNotas( Model model, @AuthenticationPrincipal(expression = "usuario") Usuario usuario, @PathVariable("id") String id){
        model.addAttribute("cita", service.obtenerCitaPorId(id));
        return "/presentation/Cita/MostrarNotas";
    }

    @PostMapping("/AceptarCita/{id}")
    public String AceptarCita(@PathVariable("id") String id){
        Cita cita = service.obtenerCitaPorId(id);
        cita.setStatus("Completada");
        service.actualizarCita(cita);
        return "redirect:/historial";
    }

    @PostMapping("/CancelarCita/{id}")
    public String CancelarCita(@PathVariable("id") String id){
        Cita cita = service.obtenerCitaPorId(id);
        cita.setStatus("Cancelada");
        service.actualizarCita(cita);
        return "redirect:/historial";
    }



}
