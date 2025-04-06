package com.tcc.rebone_3d.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByDestinatario(Usuario destinatario);
}
