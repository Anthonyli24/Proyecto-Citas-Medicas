package com.example.proyecto_sistema_citas.presentation.Login;

import com.example.proyecto_sistema_citas.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@org.springframework.stereotype.Controller("Login")
public class controller {
    @Autowired
    private Service service;

    @GetMapping("/about")
    public String About(Model modeL) {

        return "/presentation/About/about";
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

    @GetMapping("/MiPerfil")
    public String miPerfil(Model model, Principal principal) {
        String username = principal.getName();
        Usuario usuario = service.findUsuarioById(username);
        List<Horario> horarios = service.obtenerHorariosPorMedico(username);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("horarios", horarios);
        model.addAttribute("usuario", usuario);
        return "presentation/Home/MiPerfil";
    }


    @GetMapping("/")
    public String redirigir() {
        return "redirect:/home";
    }


    @GetMapping("/RegistroExitoso")
    public String RegistroExitoso(Model model) {
        return "redirect: /";
    }


}

