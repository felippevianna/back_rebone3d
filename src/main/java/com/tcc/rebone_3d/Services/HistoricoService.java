package com.tcc.rebone_3d.Services;

import com.tcc.rebone_3d.Models.Historico;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Repositories.HistoricoRepository;
import com.tcc.rebone_3d.Repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Obtém todos os históricos
    public List<Historico> getAllHistoricos() {
        return historicoRepository.findAll();
    }

    // Obtém um histórico por ID
    public Optional<Historico> getHistoricoById(Long id) {
        return historicoRepository.findById(id);
    }

    // Cria um novo histórico associado a um paciente
    public Historico createHistorico(Historico historico) {
        if (historico.getPaciente() != null && historico.getPaciente().getId() != null) {
            Optional<Paciente> pacienteOptional = pacienteRepository.findById(historico.getPaciente().getId());
            if (pacienteOptional.isPresent()) {
                historico.setPaciente(pacienteOptional.get());
            } else {
                throw new RuntimeException("Paciente não encontrado.");
            }
        } else {
            throw new RuntimeException("Paciente inválido.");
        }

        return historicoRepository.save(historico);
    }

    // Atualiza um histórico existente
    public Historico updateHistorico(Long id, Historico historicoAtualizado) {
        Optional<Historico> historicoOptional = historicoRepository.findById(id);

        if (historicoOptional.isPresent()) {
            Historico historicoExistente = historicoOptional.get();

            historicoExistente.setDescricao(historicoAtualizado.getDescricao());
            historicoExistente.setData(historicoAtualizado.getData());

            if (historicoAtualizado.getPaciente() != null && historicoAtualizado.getPaciente().getId() != null) {
                Optional<Paciente> pacienteOptional = pacienteRepository.findById(historicoAtualizado.getPaciente().getId());
                if (pacienteOptional.isPresent()) {
                    historicoExistente.setPaciente(pacienteOptional.get());
                } else {
                    throw new RuntimeException("Paciente não encontrado.");
                }
            }

            return historicoRepository.save(historicoExistente);
        } else {
            throw new RuntimeException("Histórico não encontrado.");
        }
    }

    // Deleta um histórico
    public void deleteHistorico(Long id) {
        if (historicoRepository.existsById(id)) {
            historicoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Histórico não encontrado.");
        }
    }
}
