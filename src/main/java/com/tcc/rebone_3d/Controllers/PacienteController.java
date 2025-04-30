package com.tcc.rebone_3d.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.DTO.Paciente.PacienteBuscaDTO;
import com.tcc.rebone_3d.DTO.Paciente.PacienteDTO;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Services.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Gerenciamento de Pacientes", description = "Operações relacionadas ao cadastro e gestão de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Listar todos os pacientes
    @GetMapping
    @Operation(
        summary = "Listar todos os pacientes",
        description = "Retorna a lista de pacientes associados ao usuário logado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso", content = @Content(schema = @Schema(implementation = Paciente.class)))
    })
    public ResponseEntity<List<Paciente>> listarTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<Paciente> pacientes = pacienteService.listarTodos(usuarioLogado);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    // Buscar paciente por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar paciente por ID",
        description = "Retorna os detalhes de um paciente específico mediante validação de permissões"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado", content = @Content(schema = @Schema(implementation = Paciente.class))),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
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
    @Operation(
        summary = "Cadastrar novo paciente",
        description = "Cria um novo registro de paciente no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso", content = @Content(schema = @Schema(implementation = Paciente.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Paciente> cadastrar(@RequestBody PacienteDTO paciente) {

        Paciente novoPaciente = pacienteService.cadastrar(paciente);
        return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
    }

    // Editar dados do paciente existente
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar paciente",
        description = "Atualiza os dados de um paciente existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso",
                     content = @Content(schema = @Schema(implementation = Paciente.class))),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
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
    @Operation(
        summary = "Remover paciente",
        description = "Exclui um paciente do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Paciente removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar pacientes por nome",
        description = "Retorna os pacientes do usuário logado cujo nome contenha o texto informado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrados com sucesso", content = @Content(schema = @Schema(implementation = Paciente.class)))
    })
    public ResponseEntity<List<PacienteBuscaDTO>> buscarPorNome(@RequestParam String nome) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<PacienteBuscaDTO> pacientes = pacienteService.buscarPorNomeEUsuario(nome, usuarioLogado);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

}

