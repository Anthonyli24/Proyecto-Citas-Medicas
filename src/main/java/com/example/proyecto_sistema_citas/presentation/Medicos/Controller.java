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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller("medicos")
public class Controller {
    @Autowired
    private Service service;


    @PostMapping("/filtrado/medicos")
    public String filtrarMedicos(@RequestParam(required = false) String especialidad,
                             @RequestParam(required = false) String localidad,
                             Model model) {
        List<Medico> medicosFiltrados = service.FiltradoMedicos(especialidad, localidad);

     model.addAttribute("medicos", medicosFiltrados);
        return "/presentation/home/home";
    }

    @GetMapping("/registroMedico")
    public String registroMedico(Model model) {
        return "/presentation/Registro/registroMedico";
    }

    @PostMapping("/medico/aceptar")
    public String aceptarMedico(@RequestParam("id") String id) {
        service.aceptarMedico(id);
        return "redirect:/Gestion";
    }

    @GetMapping("/Gestion")
    public String Gestion(Model model) {
        List<Medico> medicos = service.FiltradoMedicosPorStatus("Pendiente");

        model.addAttribute("medicos", medicos);
        return "/presentation/Home/gestion";
    }



    @PostMapping("/medico/actualizar")
    public String actualizarMedico(@RequestParam("id") String medicoId,
                                   @RequestParam("costo") Double costo,
                                   @RequestParam("localidad") String localidad,
                                   @RequestParam("frecuencia")Integer frecuencia)  {
        Medico medico = service.obtenerMedicoPorId(medicoId);

        medico.setCosto(costo);
        medico.setLocalidad(localidad);
        medico.setFrecuenciaCitas(frecuencia);

        service.actualizarMedico(medico);


        return "redirect:/MiPerfil?id=" + medicoId;
    }


    @GetMapping("/register/guardar/medico/{id}")
    public String mostrarFormularioMedico(@PathVariable("id") String id, Model model) {
        // Recuperar el usuario con el id proporcionado
        Usuario usuario = service.findUsuarioById(id);

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/register/guardar";
        }

        model.addAttribute("usuario", usuario);
        return "/presentation/Registro/registroMedico";
    }

    @PostMapping("/register/guardar/medico/{id}")
    public String registrarMedico(@PathVariable("id") String id,
                                  @RequestParam String especialidad,
                                  @RequestParam String costo,
                                  @RequestParam String localidad,
                                  @RequestParam String frecuencia,
                                  Model model) {
        // Recuperar el usuario con el id proporcionado
        Usuario usuario = service.findUsuarioById(id);

        // Verificar si el usuario existe
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/register/guardar"; // O lo que corresponda
        }

        // Crear una nueva entidad Médico y asignar los datos
        Medico medico = new Medico();
        medico.setUsuario(usuario);  // Asociar el usuario al médico
        medico.setEspecialidad(especialidad);
        medico.setCosto(Double.parseDouble(costo));
        medico.setLocalidad(localidad);
        medico.setFrecuenciaCitas(Integer.parseInt(frecuencia));

        // Guardar el médico en la base de datos
        service.registrarMedico(medico);

        // Redirigir a la página de éxito
        return "/presentation/Registro/registroExitoso"; // O el que corresponda
    }




}
