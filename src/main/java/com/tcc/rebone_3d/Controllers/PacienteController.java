package com.tcc.rebone_3d.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.tcc.rebone_3d.DTO.PacienteDTO;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Services.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Listar todos os pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<Paciente> pacientes = pacienteService.listarTodos(usuarioLogado);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    // Buscar paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        if(!pacienteService.PacientePodeSerAlteradoPeloUsario(id, usuarioLogado)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Optional<Paciente> paciente = pacienteService.buscarPorId(id);
            return paciente.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                           .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    // Cadastrar um novo paciente
    @PostMapping
    public ResponseEntity<Paciente> cadastrar(@RequestBody PacienteDTO paciente) {

        Paciente novoPaciente = pacienteService.cadastrar(paciente);
        return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
    }

    // Editar dados do paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> editar(@PathVariable Long id, @RequestBody Paciente pacienteAtualizado) {
        try {
            Paciente pacienteEditado = pacienteService.editar(id, pacienteAtualizado);
            return new ResponseEntity<>(pacienteEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Deletar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

