package com.tcc.rebone_3d.Models;

import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    // Endereço
    private String uf;
    private String cidade;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Historico> historicos;

    public Paciente() {}

    public Paciente(String nome, String email, String cpf, String telefone, Date dataNascimento,
                    String uf, String cidade, String cep, String rua, String numero, String bairro) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.uf = uf;
        this.cidade = cidade;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
    }
}
