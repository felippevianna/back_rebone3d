package com.tcc.rebone_3d.Controllers;

import com.tcc.rebone_3d.Models.Historico;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Repositories.HistoricoRepository;
import com.tcc.rebone_3d.Repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoController {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Endpoint para obter todos os históricos
    @GetMapping
    public List<Historico> getAllHistoricos() {
        return historicoRepository.findAll();
    }

    // Endpoint para obter um histórico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Historico> getHistoricoById(@PathVariable Long id) {
        Optional<Historico> historico = historicoRepository.findById(id);
        if (historico.isPresent()) {
            return ResponseEntity.ok(historico.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para criar um novo histórico
    @PostMapping
    public ResponseEntity<Historico> createHistorico(@RequestBody Historico historico) {
        if (historico.getPaciente() == null || historico.getPaciente().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Verifica se o paciente existe
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(historico.getPaciente().getId());
        if (!pacienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Define o paciente no histórico
        historico.setPaciente(pacienteOptional.get());

        // Salva o histórico
        Historico novoHistorico = historicoRepository.save(historico);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoHistorico);
    }

    // Endpoint para atualizar um histórico existente
    @PutMapping("/{id}")
    public ResponseEntity<Historico> updateHistorico(@PathVariable Long id, @RequestBody Historico historicoAtualizado) {
        Optional<Historico> historicoOptional = historicoRepository.findById(id);

        if (!historicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Historico historicoExistente = historicoOptional.get();
        historicoExistente.setDescricao(historicoAtualizado.getDescricao());
        historicoExistente.setData(historicoAtualizado.getData());

        // Se o paciente for alterado, verifique se o novo paciente existe
        if (historicoAtualizado.getPaciente() != null && historicoAtualizado.getPaciente().getId() != null) {
            Optional<Paciente> pacienteOptional = pacienteRepository.findById(historicoAtualizado.getPaciente().getId());
            if (pacienteOptional.isPresent()) {
                historicoExistente.setPaciente(pacienteOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

        historicoRepository.save(historicoExistente);
        return ResponseEntity.ok(historicoExistente);
    }

    // Endpoint para deletar um histórico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorico(@PathVariable Long id) {
        if (historicoRepository.existsById(id)) {
            historicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
