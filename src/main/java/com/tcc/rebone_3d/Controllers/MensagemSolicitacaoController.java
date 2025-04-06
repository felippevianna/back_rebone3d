package com.tcc.rebone_3d.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Services.MensagemSolicitacaoService;

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
    public ResponseEntity<List<MensagemSolicitacao>> listarMensagens(@PathVariable Solicitacao solicitacaoId) {
        List<MensagemSolicitacao> mensagens = mensagemSolicitacaoService.listarMensagensPorSolicitacao(solicitacaoId);
        return ResponseEntity.ok(mensagens);
    }

    @PostMapping
    public ResponseEntity<MensagemSolicitacao> salvarMensagem(@RequestBody MensagemSolicitacao mensagem) {
        return ResponseEntity.ok(mensagemSolicitacaoService.salvarMensagem(mensagem));
    }
}
