package com.tcc.rebone_3d.DTO.Historico;

import java.util.Date;
import java.util.List;

import com.tcc.rebone_3d.Models.Arquivo;
import com.tcc.rebone_3d.Models.Historico;

import io.swagger.v3.oas.annotations.media.Schema;

public record HistoricoDTOResponse(
    @Schema(
        description = "ID do histórico",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Long id,

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
    String descricao,

    @Schema(
        description = "Lista de arquivos do historico",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    List<Arquivo> arquivosDoHistorico
) {
    public static HistoricoDTOResponse fromEntity(Historico historico, List<Arquivo> arquivosDoHistorico) {
           return new HistoricoDTOResponse (
            historico.getId(),
            historico.getPaciente().getId(),
            historico.getData(),
            historico.getDescricao(),
            arquivosDoHistorico
        );
    }

    public static HistoricoDTOResponse fromEntity(Historico historico) {
        return new HistoricoDTOResponse (
         historico.getId(),
         historico.getPaciente().getId(),
         historico.getData(),
         historico.getDescricao(),
         null
     );
 }

};