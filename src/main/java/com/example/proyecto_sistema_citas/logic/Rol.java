package com.example.proyecto_sistema_citas.logic;

import java.util.Set;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "rol")
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}