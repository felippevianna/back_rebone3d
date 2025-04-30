package com.tcc.rebone_3d.DTO.Usuario;

import com.tcc.rebone_3d.Models.Enum.Perfil;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UsuarioInfoDTO",
    description = "DTO contendo informações básicas do usuário para resposta da API"
)
public record UsuarioInfoDTO(
    @Schema(
        description = "Nome de usuário único no sistema",
        example = "dr.carlos.souza",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String username,

    @Schema(
        description = "Perfil de acesso do usuário",
        implementation = Perfil.class,
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "DENTISTA",
        allowableValues = {"ADMIN", "DENTISTA", "PROTETICO"}
    )
    Perfil perfil
) {}