package com.tcc.rebone_3d.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.DTO.MensagemSolicitacaoDTO;
import com.tcc.rebone_3d.DTO.SolicitacaoDTO;
import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.UsuarioRepository;
import com.tcc.rebone_3d.Services.SolicitacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/solicitacoes")
@Tag(name = "Solicitações de usuário", description = "Endpoints para gerenciamento de solicitações enviadas entre os usuários")
@SecurityRequirement(name = "bearerAuth")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Operation(summary = "Criar solicitação", description = "Cria uma nova solicitação para outro usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitação criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Solicitacao> criarSolicitacao(@RequestBody @Parameter(description = "Dados da solicitação", required = true) SolicitacaoDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        Optional<Usuario> destinatario = usuarioRepository.findById(dto.destinatarioId());

        if(!destinatario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Solicitacao solicitacao = dto.toSolicitacaoModel(usuarioLogado, destinatario.get());

        Solicitacao novaSolicitacao = solicitacaoService.criarSolicitacao(solicitacao);
        return ResponseEntity.ok(novaSolicitacao);
    }

    @GetMapping("/recebidas")
    @Operation(summary = "Listar solicitações recebidas pelo usuário logado", description = "Lista todas as solicitações recebidas pelo usuário logado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitações encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<Solicitacao>> listarSolicitacoesRecebidas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<Solicitacao> solicitacoes = solicitacaoService.listarSolicitacoesPorDestinatario(usuarioLogado);
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitação por ID", description = "Retorna uma solicitação pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public ResponseEntity<Solicitacao> buscarSolicitacaoPorId(
        @PathVariable @Parameter(description = "ID da solicitação") Long id) {

        Optional<Solicitacao> solicitacao = solicitacaoService.buscarSolicitacaoPorId(id);
        return solicitacao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/responder")
    @Operation(summary = "Responder solicitação", description = "Adiciona uma resposta a uma solicitação existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensagem adicionada"),
        @ApiResponse(responseCode = "400", description = "Erro ao adicionar a mensagem")
    })
    public ResponseEntity<MensagemSolicitacao> responderSolicitacao(@PathVariable @Parameter(description = "ID da solicitação") Long id,
        @RequestBody @Parameter(description = "Conteúdo da resposta") MensagemSolicitacaoDTO mensagemDto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        MensagemSolicitacao novaMensagem = solicitacaoService.adicionarMensagem(id, mensagemDto, usuarioLogado);
        if (novaMensagem != null) {
            return ResponseEntity.ok(novaMensagem);
        }
        return ResponseEntity.badRequest().build();
    }
}
