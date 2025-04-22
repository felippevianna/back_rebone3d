package com.tcc.rebone_3d.DTO.Historico;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "HistóricoDTO",
    description = "DTO para criação/atualização de registros de histórico médico"
)
public record HistoricoDTO(
    @Schema(
        description = "ID do paciente associado ao histórico",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Long idPaciente,

    @Schema(
        description = "Data do registro médico (formato: yyyy-MM-dd)",
        example = "2024-06-20",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",  // Força exibição como string no Swagger
        format = "date"   // Indica formato de data
    )
    Date data,

    @Schema(
        description = "Descrição detalhada do histórico médico",
        example = "Paciente apresentou melhora significativa na mobilidade articular",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 1000
    )
    String descricao
) {}