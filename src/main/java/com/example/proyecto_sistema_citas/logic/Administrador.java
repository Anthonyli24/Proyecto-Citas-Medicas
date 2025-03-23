package com.example.proyecto_sistema_citas.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "administrador")
public class Administrador {
    @Id
    @Size(max = 100)
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Usuario usuario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
