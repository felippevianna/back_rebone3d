package com.tcc.rebone_3d.DTO;

import java.util.Date;
import com.tcc.rebone_3d.Models.Arquivo;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArquivoDTOResponse(
    @Schema(description = "ID do arquivo", example = "10")
    Long id,

    @Schema(description = "Caminho do arquivo salvo", example = "/uploads/raiox123.png")
    String caminhoArquivo,

    @Schema(description = "Data de upload do arquivo", example = "2024-06-20", format = "date")
    Date dataUpload
) {
    public static ArquivoDTOResponse fromEntity(Arquivo arquivo) {
        return new ArquivoDTOResponse(
            arquivo.getId(),
            arquivo.getCaminhoArquivo(),
            arquivo.getDataUpload()
        );
    }
}
