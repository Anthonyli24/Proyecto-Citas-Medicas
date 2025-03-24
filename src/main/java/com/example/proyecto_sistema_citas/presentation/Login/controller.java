package com.example.proyecto_sistema_citas.presentation.Login;

import com.example.proyecto_sistema_citas.data.MedicoRepository;
import com.example.proyecto_sistema_citas.logic.Medico;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import com.example.proyecto_sistema_citas.logic.Rol;
import com.example.proyecto_sistema_citas.presentation.Security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@org.springframework.stereotype.Controller("Login")
public class controller {
    @Autowired
    private Service service;

    @GetMapping("/about")
    public String About(Model modeL) {

        return "/presentation/About/about";
    }

    @GetMapping("/registro")
    public String Form(Model model) {

        Iterable<Rol> roles = service.rolFindAll();

        // Agregar un nuevo objeto Usuario al modelo para el formulario
        model.addAttribute("usuario", new Usuario());

        // Pasar la lista de roles al modelo
        model.addAttribute("roles", roles);
        return "/presentation/Registro/registro";
    }

    @GetMapping("/registroMedico")
    public String registroMedico(Model model) {
        return "/presentation/Registro/registroMedico";
    }

    @GetMapping("/login")
    public String Login(Model model) {
        return "/presentation/Login/login";
    }
    
    @PostMapping("/login")
    public String acceder(){
        return "/presentation/Home/home";
    }

    @GetMapping("/css")
    public String CSS(Model model) {
        return "/static/css";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized(Model model) {
        return "redirect:/presentation/Login/notAuthorized";
    }

    @GetMapping("/")
    public String redirigir() {
        return "redirect:/home";
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



    @GetMapping("/register/guardar/medico/{id}")
    public String mostrarFormularioMedico(@PathVariable("id") String id, Model model) {
        // Recuperar el usuario con el id proporcionado
        Usuario usuario = service.findUsuarioById(id);

        // Verificar si el usuario existe
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/register/guardar"; // O lo que corresponda
        }

        // Pasar el usuario a la vista
        model.addAttribute("usuario", usuario);
        return "/presentation/Registro/registroMedico"; // Formulario para registrar el médico
    }

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

        // Separar fecha y hora del parámetro `ddt`
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

    @GetMapping("/book")
    public String redirigirReserva(@RequestParam("did") String doctorId,
                                   @RequestParam("ddt") String dateTime) {
        String encodedDoctorId = URLEncoder.encode(doctorId, StandardCharsets.UTF_8);
        String encodedDateTime = URLEncoder.encode(dateTime, StandardCharsets.UTF_8);

        return "redirect:/confirmar?did=" + encodedDoctorId + "&ddt=" + encodedDateTime;
    }


    @GetMapping("/RegistroExitoso")
    public String RegistroExitoso(Model model) {
        return "redirect: /";
    }
}