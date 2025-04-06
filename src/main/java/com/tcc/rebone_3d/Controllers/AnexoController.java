package com.tcc.rebone_3d.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcc.rebone_3d.Models.Anexo;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Services.AnexoService;
import com.tcc.rebone_3d.Services.SolicitacaoService;

/* Alterar a parte de anexos para que consiga salvar os arquivos na pasta corretamente. Igual ao controller de histórico
 * A ideia é colocar todos os uploads para o firebase.
 *
 */
@RestController
@RequestMapping("/api/anexos")
public class AnexoController {

    private final AnexoService anexoService;
    private final SolicitacaoService solicitacaoService;

    public AnexoController(AnexoService anexoService, SolicitacaoService solicitacaoService) {
        this.anexoService = anexoService;
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping("/{solicitacaoId}")
    public ResponseEntity<Anexo> uploadAnexo(@PathVariable Long solicitacaoId, @RequestParam("file") MultipartFile file) {
        Optional<Solicitacao> solicitacao = solicitacaoService.buscarSolicitacaoPorId(solicitacaoId);

        if (solicitacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Anexo anexo = anexoService.salvarAnexo(file, solicitacao.get());
            return ResponseEntity.ok(anexo);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{solicitacaoId}")
    public ResponseEntity<List<Anexo>> listarAnexos(@PathVariable Long solicitacaoId) {
        Optional<Solicitacao> solicitacao = solicitacaoService.buscarSolicitacaoPorId(solicitacaoId);

        if (solicitacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Anexo> anexos = anexoService.listarAnexosPorSolicitacao(solicitacao.get());
        return ResponseEntity.ok(anexos);
    }
}
