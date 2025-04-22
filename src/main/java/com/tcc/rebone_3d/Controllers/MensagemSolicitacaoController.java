package com.tcc.rebone_3d.Controllers;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.DTO.Mensagem.MensagemSolicitacaoDTO;
import com.tcc.rebone_3d.DTO.Mensagem.MensagemSolicitacaoDTOResponse;
import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Services.MensagemSolicitacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mensagens")
@Tag(name = "Mensagens das solicitações", description = "Endpoints para gerenciamento de mensagens dentro das solicitações enviadas entre os usuários")
@SecurityRequirement(name = "bearerAuth")
public class MensagemSolicitacaoController {

    @Autowired
    private MensagemSolicitacaoService mensagemSolicitacaoService;


    @GetMapping("/{solicitacaoId}")
    @Operation(
        summary = "Listar mensagens da solicitação",
        description = "Retorna todas as mensagens enviadas entre os usuários da solicitação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensagens encontrados"),
        @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content)
    })
    public ResponseEntity<List<MensagemSolicitacaoDTOResponse>> listarMensagens(@PathVariable Long solicitacaoId) {
        List<MensagemSolicitacao> mensagens = mensagemSolicitacaoService.listarMensagensPorSolicitacaoId(solicitacaoId);

        List<MensagemSolicitacaoDTOResponse> mensagensDTO = mensagens.stream()
            .map(MensagemSolicitacaoDTOResponse::fromEntity)
            .toList();
    
        return ResponseEntity.ok(mensagensDTO);
    }

    @PostMapping
    @Operation(
        summary = "Salvar nova mensagem",
        description = "Salva uma nova mensagem na solicitação desejada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensagem Salva"),
        @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content)
    })
    public ResponseEntity<MensagemSolicitacaoDTOResponse> salvarMensagem(@RequestBody MensagemSolicitacaoDTO mensagemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        MensagemSolicitacao salva = mensagemSolicitacaoService.salvarMensagem(mensagemDto, usuarioLogado);
        return ResponseEntity.ok(MensagemSolicitacaoDTOResponse.fromEntity(salva));
    }
}
