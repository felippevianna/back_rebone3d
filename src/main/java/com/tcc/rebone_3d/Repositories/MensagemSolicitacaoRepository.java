package com.tcc.rebone_3d.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;

public interface MensagemSolicitacaoRepository extends JpaRepository<MensagemSolicitacao, Long> {
    List<MensagemSolicitacao> findBySolicitacao(Solicitacao solicitacao);
}
