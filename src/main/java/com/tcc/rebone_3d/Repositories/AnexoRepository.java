package com.tcc.rebone_3d.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcc.rebone_3d.Models.Anexo;
import com.tcc.rebone_3d.Models.Solicitacao;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {
    List<Anexo> findBySolicitacao(Solicitacao solicitacao);
}
