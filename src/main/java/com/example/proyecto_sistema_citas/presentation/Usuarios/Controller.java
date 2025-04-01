package com.example.proyecto_sistema_citas.presentation.Usuarios;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.proyecto_sistema_citas.logic.Medico;
import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.proyecto_sistema_citas.logic.Rol;
import org.springframework.web.multipart.MultipartFile;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@org.springframework.stereotype.Controller("usuarios")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/home")
    public String ListarUsuarios(@RequestParam(value = "semana", required = false, defaultValue = "0") int semana, Model model, @RequestParam(value = "error", required = false) String error) {
        Map<String, Map<LocalDate, List<String>>> disponibilidad = new HashMap<>();

        for (Medico medico : service.medicoFindAll()) {
            Map<LocalDate, List<String>> fechas = medico.getFechas(semana);
            disponibilidad.put(medico.getId(), fechas);
        }

        model.addAttribute("medicos", service.medicoFindAll());
        model.addAttribute("disponibilidad", disponibilidad);
        model.addAttribute("semana", semana);

        if (error != null) {
            model.addAttribute("error", "El horario seleccionado ya está ocupado. Por favor, elige otro.");
        }

        return "/presentation/Home/home";
    }

    @GetMapping("/registro")
    public String Form(Model model) {
        Iterable<Rol> roles = service.rolFindAll();

        model.addAttribute("usuario", new Usuario());

        model.addAttribute("roles", roles);
        return "/presentation/Registro/registro";
    }

    @PostMapping("/register/guardar")
    public String RegistroUsuario(@ModelAttribute Usuario usuario,
                                  @RequestParam("imagen") MultipartFile imagen,
                                  Model model) throws IOException {
        if (service.existeUsuarioPorId(usuario.getId())) {
            model.addAttribute("error", "El usuario con este ID ya está registrado.");
            return "/presentation/Login/login";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encriptada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(encriptada);

        if (!imagen.isEmpty()) {
            String nombreArchivo = usuario.getId() + ".jpg";

            String documentosDir = System.getProperty("user.home") + "/Documents/Usuarios/";

            Path directorioDestino = Paths.get(documentosDir);
            if (!Files.exists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
            }

            Path rutaArchivo = directorioDestino.resolve(nombreArchivo);
            imagen.transferTo(rutaArchivo.toFile());
        }

        if ("Medico".equalsIgnoreCase(usuario.getRol().getNombre())) {
            model.addAttribute("usuario", usuario);
            service.RegistrarUsuario(usuario);
            return "redirect:/register/guardar/medico/" + usuario.getId();
        }
        service.RegistrarUsuario(usuario);
        return "/presentation/Registro/registroExitoso";
    }

    @GetMapping("/usuario/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable("id") String id) {
        Path pathImagen = Paths.get(System.getProperty("user.home") + "/Documents/Usuarios/" + id + ".jpg");
        if (Files.exists(pathImagen)) {
            try {
                byte[] imagenBytes = Files.readAllBytes(pathImagen);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imagenBytes);
            } catch (IOException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}