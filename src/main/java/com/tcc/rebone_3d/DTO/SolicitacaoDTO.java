package com.tcc.rebone_3d.DTO;

import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Models.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SolicitacaoDTO", description = "DTO para criação de solicitações")
public record SolicitacaoDTO(
    @Schema(description = "ID do destinatário da solicitação", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    Long destinatarioId,

    @Schema(description = "Título da solicitação", example = "Preciso de ajuda com o laudo", requiredMode = Schema.RequiredMode.REQUIRED)
    String titulo,

    @Schema(description = "Descrição da solicitação", example = "Pode verificar a imagem de raio-x?", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String descricao
) {

    public Solicitacao toSolicitacaoModel (Usuario remetente, Usuario destinatario){
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setTitulo(this.titulo);
        solicitacao.setDescricao(this.descricao);
        solicitacao.setRemetente(remetente);
        solicitacao.setDestinatario(destinatario);

        return solicitacao;
    }

}

