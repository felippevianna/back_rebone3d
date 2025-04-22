package com.tcc.rebone_3d.Models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private Date dataCriacao;

    @ManyToOne
    @JoinColumn(name = "remetente_id")
    @JsonManagedReference
    private Usuario remetente; // Usuário que enviou a solicitação

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    @JsonManagedReference
    private Usuario destinatario; // Usuário que recebeu a solicitação

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Anexo> anexos;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<MensagemSolicitacao> mensagens;

    public Solicitacao() {
        this.dataCriacao = new Date();
    }
}