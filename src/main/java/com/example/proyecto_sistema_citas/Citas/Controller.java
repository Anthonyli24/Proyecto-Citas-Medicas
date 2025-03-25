package com.example.proyecto_sistema_citas.Citas;


import com.example.proyecto_sistema_citas.logic.Cita;
import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@org.springframework.stereotype.Controller("Citas")
public class Controller {

    @Autowired
    private Service service;


    @GetMapping("/confirmar")
    public String confirmar(@RequestParam("did") String doctorId,
                            @RequestParam("ddt") String dateTime,
                            Model model) {
        Medico medico = service.obtenerMedicoPorId(doctorId);

        if (medico != null) {
            model.addAttribute("medico", medico);
        } else {
            model.addAttribute("error", "Médico no encontrado");
        }

        String[] dateTimeParts = dateTime.split("T");
        if (dateTimeParts.length == 2) {
            model.addAttribute("fecha", dateTimeParts[0]);
            model.addAttribute("hora", dateTimeParts[1]);
        } else {
            model.addAttribute("fecha", "Fecha no válida");
            model.addAttribute("hora", "Hora no válida");
        }

        return "presentation/Home/confirmar";
    }



    @GetMapping("/appointment/confirm")
    public String AgendarCita(@RequestParam("did") String medicoId, // Cambiar a String para manejar identificadores como "M001"
                              @RequestParam("ddt") String fechaHora, // Recibe el parámetro 'ddt' que contiene "fechaTtime"
                              @AuthenticationPrincipal(expression = "usuario") Usuario usuario) {

        Cita cita = new Cita();
        cita.setMedico(service.obtenerMedicoPorId(medicoId)); // El ID es un String, no Long
        cita.setUsuario(usuario);
        String[] fechaHoraParts = fechaHora.split("T");
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("d/M/yy");
        LocalDate fecha = LocalDate.parse(fechaHoraParts[0], formatterFecha);
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime hora = LocalTime.parse(fechaHoraParts[1], formatterHora);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setStatus("Pendiente");
        service.agendarCita(cita);
        return "/presentation/Home/home";
    }


    @GetMapping("/book")
    public String redirigirReserva(@RequestParam("did") String doctorId,
                                   @RequestParam("ddt") String dateTime) {
        String encodedDoctorId = URLEncoder.encode(doctorId, StandardCharsets.UTF_8);
        String encodedDateTime = URLEncoder.encode(dateTime, StandardCharsets.UTF_8);

        return "redirect:/confirmar?did=" + encodedDoctorId + "&ddt=" + encodedDateTime;
    }

    @GetMapping("/citas/filtro")
    public String FiltroCitas(@RequestParam(defaultValue = "Pendiente") String status,
                              @RequestParam(defaultValue = "") String doctor, Model model){

        Iterable<Cita> citas = service.FiltradoCitas(status, doctor);


        model.addAttribute("citas", citas);
        return "/presentation/Cita/historial";
    }
}
