package com.example.proyecto_sistema_citas.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Size(max = 100)
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "clave", nullable = false)
    private String clave;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @OneToOne(mappedBy = "usuario")
    private Medico medico;

    @OneToOne(mappedBy = "usuario")
    private Administrador administrador;

    public String getId() {
        return id;
    }
    public String getClave() {
        return clave;
    }
    public Rol getRol() {
        return rol;
    }
    public String getNombre() {
        return nombre;
    }
    public Medico getMedico() {
        return medico;
    }
    public Administrador getAdministrador() {
        return administrador;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}