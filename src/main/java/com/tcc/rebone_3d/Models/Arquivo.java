package com.tcc.rebone_3d.Models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caminhoArquivo; // Caminho onde a imagem está salva
    private Date dataUpload;       // Data de upload da imagem

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario profissional;  // Profissional que fez o upload

    @ManyToOne
    @JoinColumn(name = "historico_id")
    private Historico historico; // Agendamento vinculado à imagem

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;     // Paciente vinculado à imagem

    public Arquivo() {}

    public Arquivo(String caminhoArquivo, Date dataUpload, Usuario profissional, Historico historico, Paciente paciente) {
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = dataUpload;
        this.profissional = profissional;
        this.historico = historico;
        this.paciente = paciente;
    }
}