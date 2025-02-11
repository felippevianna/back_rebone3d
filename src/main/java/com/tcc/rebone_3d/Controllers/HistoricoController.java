package com.tcc.rebone_3d.Controllers;

import com.tcc.rebone_3d.DTO.HistoricoDTO;
import com.tcc.rebone_3d.Models.Historico;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.HistoricoRepository;
import com.tcc.rebone_3d.Repositories.PacienteRepository;
import com.tcc.rebone_3d.Services.HistoricoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private HistoricoService historicoService;

    // Endpoint para obter todos os históricos
    @GetMapping
    public ResponseEntity<List<Historico>> getAllHistoricos(Long idPaciente) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<Historico> historicos = historicoService.listarTodosHistoricosDoPaciente(usuarioLogado, idPaciente);

        return new ResponseEntity<>(historicos, HttpStatus.OK);
    }

    // Endpoint para obter um histórico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Historico> getHistoricoById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        if(!historicoService.HistoricoPodeSerAlteradoPeloUsario(id, usuarioLogado)) {
            Optional<Historico> historico = historicoRepository.findById(id);
            if (historico.isPresent()) {
                return ResponseEntity.ok(historico.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    // Endpoint para criar um novo histórico
    @PostMapping
    public ResponseEntity<Historico> createHistorico(@RequestBody HistoricoDTO historicoDto) {
        if (historicoDto.idPaciente() == null || historicoDto.data() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Verifica se o paciente existe
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(historicoDto.idPaciente());
        if (!pacienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Historico historico = new Historico();
        // Define o paciente no histórico
        historico.setPaciente(pacienteOptional.get());
        historico.setData(historicoDto.data());
        historico.setDescricao(historicoDto.descricao());

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
