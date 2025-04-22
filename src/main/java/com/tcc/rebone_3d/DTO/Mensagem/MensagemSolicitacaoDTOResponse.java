package com.tcc.rebone_3d.DTO.Mensagem;

import java.util.Date;

import com.tcc.rebone_3d.Models.MensagemSolicitacao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MensagemSolicitacaoDTO", description = "DTO para retornar a mensagem de uma solicitação")
public record MensagemSolicitacaoDTOResponse(
    @Schema(
        description = "ID da Mensagem",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Long id,

    @Schema(
        description = "Texto da mensagem", 
        example = "Claro, já estou analisando.", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String mensagem,

    @Schema(
        description = "Data de envio da mensagem (formato: yyyy-MM-dd)",
        example = "2024-06-20",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",  // Força exibição como string no Swagger
        format = "date"   // Indica formato de data
    )
    Date dataEnvio,

    @Schema(
        description = "ID do Usuário que enviou a mensagem",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Long usuarioId,

    @Schema(
        description = "Nome do usuárioq ue enviou a mensagem", 
        example = "João Silva", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String usuarioNome,

    @Schema(
        description = "ID da solicitação",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
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

