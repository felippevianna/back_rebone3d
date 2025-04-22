package com.tcc.rebone_3d.Models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Date data;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @JsonManagedReference
    private Paciente paciente;

    @OneToMany(mappedBy = "historico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Arquivo> arquivos; // Lista de imagens vinculadas ao histórico

    public Historico() {}

    public Historico(String descricao, Date data, Paciente paciente) {
        this.descricao = descricao;
        this.data = data;
        this.paciente = paciente;
    }
}
