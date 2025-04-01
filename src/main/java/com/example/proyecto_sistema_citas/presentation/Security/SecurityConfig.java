package com.example.proyecto_sistema_citas.presentation.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(customizer->customizer
                .requestMatchers("/","/listado/medicos","/about","/home","/login","/registro","/registroMedico","/register/RegistroExitoso","/register/guardar",
                        "/register/guardar/medico/{id}","/login/acceder","/css/**","/images/**", "/usuario/imagen/**", "/filtrado/medicos",
                        "/appointments", "/notAuthorized").permitAll()
               // .requestMatchers("/Gestion").hasAuthority("3")
                //.requestMatchers("/historial").hasAuthority("2")
                        .anyRequest().authenticated())
                .formLogin(customizer->customizer.loginPage("/login").defaultSuccessUrl("/home", true).permitAll())
                .logout(customizer->customizer.permitAll().logoutSuccessUrl("/Login").invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(customizer->customizer.accessDeniedPage("/notAuthorized"))
                .csrf(customizer->customizer.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}