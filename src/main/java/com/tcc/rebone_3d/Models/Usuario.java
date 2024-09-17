package com.tcc.rebone_3d.Models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String perfil; // Exemplo de perfis: "ADMIN", "DENTISTA", "PROTETICO????"

    // Construtor vazio
    public Usuario() {}

    // Construtor com parâmetros
    public Usuario(String nome, String email, String senha, String perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }
}
