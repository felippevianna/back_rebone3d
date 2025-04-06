package com.tcc.rebone_3d.DTO;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import com.tcc.rebone_3d.Models.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MensagemSolicitacaoDTO", description = "DTO para responder a uma solicitação")
public record MensagemSolicitacaoDTO(
    @Schema(description = "Texto da mensagem", example = "Claro, já estou analisando.", requiredMode = Schema.RequiredMode.REQUIRED)
    String mensagem
) {
    public MensagemSolicitacao toMensagemSolicitacaoModel(Usuario usuario) {
        MensagemSolicitacao mensagemSolicitacao = new MensagemSolicitacao();
        mensagemSolicitacao.setUsuario(usuario);
        mensagemSolicitacao.setMensagem(mensagem);

        return mensagemSolicitacao;
    }
}
