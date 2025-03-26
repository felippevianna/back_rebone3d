package com.tcc.rebone_3d.DTO;

import com.tcc.rebone_3d.Models.Enum.Perfil;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "RegisterDTO",
    description = "DTO para registro de novos usuários no sistema"
)
public record RegisterDTO(
    @Schema(
        description = "Nome de usuário para login",
        example = "medico.renato",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 4,
        maxLength = 20
    )
    String username,

    @Schema(
        description = "Senha de acesso",
        example = "S3nhaF0rte@2024",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 8,
        format = "password"
    )
    String password,

    @Schema(
        description = "Perfil de acesso do usuário",
        implementation = Perfil.class,
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "DENTISTA",
        allowableValues = {"ADMIN", "DENTISTA", "PROTETICO"}
    )
    Perfil perfil
) {}