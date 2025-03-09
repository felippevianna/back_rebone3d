package com.tcc.rebone_3d.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Imagem {

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

    public Imagem() {}

    public Imagem(String caminhoArquivo, Date dataUpload, Usuario profissional, Historico historico, Paciente paciente) {
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = dataUpload;
        this.profissional = profissional;
        this.historico = historico;
        this.paciente = paciente;
    }
}