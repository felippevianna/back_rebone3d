package com.tcc.rebone_3d.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.rebone_3d.Models.Historico;
import com.tcc.rebone_3d.Models.Paciente;

import java.util.List;


@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    // Métodos customizados podem ser adicionados aqui, se necessário

    List<Historico> findByPaciente(Paciente paciente);
}