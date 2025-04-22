package com.tcc.rebone_3d.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.MensagemSolicitacaoRepository;
import com.tcc.rebone_3d.Repositories.SolicitacaoRepository;
import com.tcc.rebone_3d.DTO.Mensagem.MensagemSolicitacaoDTO;

@Service
public class MensagemSolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;
    
    @Autowired
    private MensagemSolicitacaoRepository mensagemSolicitacaoRepository;

    public MensagemSolicitacaoService(MensagemSolicitacaoRepository mensagemSolicitacaoRepository) {
        this.mensagemSolicitacaoRepository = mensagemSolicitacaoRepository;
    }

    public MensagemSolicitacao salvarMensagem(MensagemSolicitacaoDTO mensagemDto, Usuario usuarioLogado) {
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(mensagemDto.solicitacaoId());
        if (solicitacaoOptional.isPresent()) {
            MensagemSolicitacao mensagem = mensagemDto.toMensagemSolicitacaoModel(usuarioLogado);
            mensagem.setSolicitacao(solicitacaoOptional.get());
            return mensagemSolicitacaoRepository.save(mensagem);
        }
        return null;
    }

    public List<MensagemSolicitacao> listarMensagensPorSolicitacaoId(Long solicitacaoId) {
        Optional<Solicitacao> solicitacaoOpt = solicitacaoRepository.findById(solicitacaoId);
    
        if (solicitacaoOpt.isEmpty()) {
            throw new RuntimeException("Solicitação não encontrada com o ID: " + solicitacaoId);
        }
    
        return mensagemSolicitacaoRepository.findBySolicitacao(solicitacaoOpt.get());
    }

    public Optional<MensagemSolicitacao> buscarMensagemPorId(Long id) {
        return mensagemSolicitacaoRepository.findById(id);
    }
}
