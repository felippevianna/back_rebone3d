package com.tcc.rebone_3d.DTO.Mensagem;

import java.util.Date;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;

public record MensagemSolicitacaoDTOResponse(
    Long id,
    String mensagem,
    Date dataEnvio,
    Long usuarioId,
    String usuarioNome,
    Long idSolicitacao
) {
    public static MensagemSolicitacaoDTOResponse fromEntity(MensagemSolicitacao mensagem) {
        return new MensagemSolicitacaoDTOResponse(
            mensagem.getId(),
            mensagem.getMensagem(),
            mensagem.getDataEnvio(),
            mensagem.getUsuario().getId(),
            mensagem.getUsuario().getUsername(),
            mensagem.getSolicitacao().getId()
        );
    }
}

