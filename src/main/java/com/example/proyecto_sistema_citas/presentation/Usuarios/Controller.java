package com.example.proyecto_sistema_citas.presentation.Usuarios;

import com.example.proyecto_sistema_citas.logic.Rol;
import com.example.proyecto_sistema_citas.logic.Service;
import com.example.proyecto_sistema_citas.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@org.springframework.stereotype.Controller("usuarios")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/home")
    public String ListarUsuarios(Model model) {
        model.addAttribute("medicos", service.medicoFindAll());
        return "/presentation/Home/home";
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
