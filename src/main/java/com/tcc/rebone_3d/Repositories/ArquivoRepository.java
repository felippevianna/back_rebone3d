package com.tcc.rebone_3d.Repositories;

import com.tcc.rebone_3d.Models.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    List<Arquivo> findByPacienteIdOrderByDataUploadDesc(Long pacienteId); // Busca arquivos de um paciente ordenadas por data

    List<Arquivo> findByHistoricoId(Long historicoId); // Busca arquivos de um historico
}