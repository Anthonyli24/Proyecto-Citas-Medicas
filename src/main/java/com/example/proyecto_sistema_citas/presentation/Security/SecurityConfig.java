package com.example.proyecto_sistema_citas.presentation.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(customizer->customizer
                .requestMatchers("/","/listado/medicos","/about","/home","/login","/registro","/registroMedico","/register/RegistroExitoso","/register/guardar","/register/guardar/medico/{id}","/login/acceder","/css/**","/images/**").permitAll()
                .requestMatchers("/registro/medicos/guardar").hasAuthority("3").anyRequest().authenticated())
                .formLogin(customizer->customizer.loginPage("/login").permitAll())
                .logout(customizer->customizer.permitAll().logoutSuccessUrl("/"))
                .exceptionHandling(customizer->customizer.accessDeniedPage("/notAuthorized"))
                .csrf(customizer->customizer.disable());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
