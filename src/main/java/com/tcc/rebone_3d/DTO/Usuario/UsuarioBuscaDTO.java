package com.tcc.rebone_3d.DTO.Usuario;

import com.tcc.rebone_3d.Models.Enum.Perfil;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UsuarioBuscaDTO",
    description = "DTO contendo informações básicas do usuário para resposta da API"
)
public record UsuarioBuscaDTO(
    @Schema(
        description = "Nome de usuário único no sistema",
        example = "dr.carlos.souza",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String username,

    @Schema(
        description = "ID do usuário",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "1"
    )
    Long id
) {}