package com.tcc.rebone_3d.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.rebone_3d.Models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Métodos customizados podem ser adicionados aqui, se necessário
}
