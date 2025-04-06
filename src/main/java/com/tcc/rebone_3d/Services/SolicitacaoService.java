package com.tcc.rebone_3d.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.rebone_3d.DTO.MensagemSolicitacaoDTO;
import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.MensagemSolicitacaoRepository;
import com.tcc.rebone_3d.Repositories.SolicitacaoRepository;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private MensagemSolicitacaoRepository mensagemSolicitacaoRepository;

    public Solicitacao criarSolicitacao(Solicitacao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> listarSolicitacoesPorDestinatario(Usuario destinatario) {
        return solicitacaoRepository.findByDestinatario(destinatario);
    }

    public Optional<Solicitacao> buscarSolicitacaoPorId(Long id) {
        return solicitacaoRepository.findById(id);
    }

    public MensagemSolicitacao adicionarMensagem(Long solicitacaoId, MensagemSolicitacaoDTO mensagemDto, Usuario usuario) {
        Optional<Solicitacao> solicitacaoOptional = solicitacaoRepository.findById(solicitacaoId);
        if (solicitacaoOptional.isPresent()) {
            MensagemSolicitacao mensagem = mensagemDto.toMensagemSolicitacaoModel(usuario);
            mensagem.setSolicitacao(solicitacaoOptional.get());
            return mensagemSolicitacaoRepository.save(mensagem);
        }
        return null;
    }
}
