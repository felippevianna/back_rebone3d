package com.tcc.rebone_3d.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.DTO.Usuario.UsuarioBuscaDTO;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name="Gerenciamento de Usuários", description = "Operações relacionadas aos usuários do sistema")
@SecurityRequirement(name = "bearerAuth")
public class UsuariosController {
    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar usuários por nome",
        description = "Retorna os usuários cujo nome contenha o texto informado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuáriops encontrados com sucesso", content = @Content(schema = @Schema(implementation = Usuario.class)))
    })
    public ResponseEntity<List<UsuarioBuscaDTO>> buscarPorNome(@RequestParam String nome) {
        List<UsuarioBuscaDTO> usuarios = usuarioService.buscarPorNome(nome);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}
