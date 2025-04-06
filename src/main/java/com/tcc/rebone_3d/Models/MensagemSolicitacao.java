package com.tcc.rebone_3d.Models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class MensagemSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;
    private Date dataEnvio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonManagedReference
    private Usuario usuario; // Quem enviou a mensagem

    @ManyToOne
    @JoinColumn(name = "solicitacao_id")
    @JsonBackReference
    private Solicitacao solicitacao;

    public MensagemSolicitacao() {
        this.dataEnvio = new Date();
    }
}
