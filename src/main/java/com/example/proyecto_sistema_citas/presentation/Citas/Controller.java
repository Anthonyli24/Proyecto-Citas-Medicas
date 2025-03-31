package com.example.proyecto_sistema_citas.presentation.Citas;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.ui.Model;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import com.example.proyecto_sistema_citas.logic.Cita;
import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    public String AgendarCita(@RequestParam("did") String medicoId,
                              @RequestParam("ddt") String fechaHora,
                              @AuthenticationPrincipal(expression = "usuario") Usuario usuario) {
        Cita cita = new Cita();
        cita.setMedico(service.obtenerMedicoPorId(medicoId));
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
        return "redirect:/home";
    }

    @GetMapping("/book")
    public String redirigirReserva(@RequestParam("did") String doctorId,
                                   @RequestParam("ddt") String dateTime) {
        String encodedDoctorId = URLEncoder.encode(doctorId, StandardCharsets.UTF_8);
        String encodedDateTime = URLEncoder.encode(dateTime, StandardCharsets.UTF_8);

        return "redirect:/confirmar?did=" + encodedDoctorId + "&ddt=" + encodedDateTime;
    }

    @GetMapping("/citas/filtro")
    public String FiltroCitas(@RequestParam(value = "status", required = false) String status,
                              @RequestParam(value = "doctor", defaultValue = "") String doctor, Model model,
                              @AuthenticationPrincipal(expression = "usuario") Usuario usuario){

        if (status == null || status.isEmpty()) {
            status = "";
        }

        Iterable<Cita> citas = service.FiltradoCitas(status, doctor, usuario.getId());
        model.addAttribute("CitasMedico", citas);
        return "/presentation/Cita/historial";
    }

    @GetMapping("/agregarNotas/{id}")
    public String AgregarNotas(@PathVariable("id") String id, Model model){
        Cita cita = service.obtenerCitaPorId(id);
        model.addAttribute("cita", cita);
        model.addAttribute("Notas");
        return "/presentation/Cita/agregarNotas";
    }

    @PostMapping("/agregarNotas/{id}")
    public String guardarNotas(@PathVariable("id") String id, @RequestParam("nota") String nota) {
        Cita cita = service.obtenerCitaPorId(id);
        cita.setNotas(nota);
        service.actualizarCita(cita);
        return "redirect:/historial";
    }

    @GetMapping("/citas/filtro/paciente")
    public String filtroPacientes(@RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "paciente", defaultValue = "") String doctor, Model model,
                                  @AuthenticationPrincipal(expression = "usuario") Usuario usuario) {

        if (status == null || status.isEmpty()) {
            status = "";
        }

        Iterable<Cita> citas = service.FiltradoCitas("Completado", doctor, usuario.getId());
        model.addAttribute("CitasPaciente", citas);
        System.out.println("citas: " + citas);
        return "/presentation/Cita/historialPaciente";
    }
}