package com.tcc.rebone_3d.Repositories;

import com.tcc.rebone_3d.Models.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    List<Imagem> findByPacienteIdOrderByDataUploadDesc(Long pacienteId); // Busca imagens de um paciente ordenadas por data
}