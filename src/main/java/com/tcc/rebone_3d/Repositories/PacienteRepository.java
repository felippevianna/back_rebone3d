package com.tcc.rebone_3d.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import java.util.List;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Métodos customizados podem ser adicionados aqui, se necessário
   Paciente findByIdAndUsuarioResponsavel(Long id, Usuario usuarioResponsavel);

   List<Paciente> findByUsuarioResponsavel(Usuario usuarioResponsavel);

   List<Paciente> findByNomeContainingIgnoreCaseAndUsuarioResponsavel(String nome, Usuario usuario);

}
