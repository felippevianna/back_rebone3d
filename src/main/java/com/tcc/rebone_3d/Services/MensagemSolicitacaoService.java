package com.tcc.rebone_3d.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Repositories.MensagemSolicitacaoRepository;

@Service
public class MensagemSolicitacaoService {

    private final MensagemSolicitacaoRepository mensagemSolicitacaoRepository;

    public MensagemSolicitacaoService(MensagemSolicitacaoRepository mensagemSolicitacaoRepository) {
        this.mensagemSolicitacaoRepository = mensagemSolicitacaoRepository;
    }

    public MensagemSolicitacao salvarMensagem(MensagemSolicitacao mensagem) {
        return mensagemSolicitacaoRepository.save(mensagem);
    }

    public List<MensagemSolicitacao> listarMensagensPorSolicitacao(Solicitacao solicitacao) {
        return mensagemSolicitacaoRepository.findBySolicitacao(solicitacao);
    }

    public Optional<MensagemSolicitacao> buscarMensagemPorId(Long id) {
        return mensagemSolicitacaoRepository.findById(id);
    }
}
