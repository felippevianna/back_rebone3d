package com.tcc.rebone_3d.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Credenciais de Autenticação",
    description = "DTO para receber credenciais de login"
)
public record AuthenticationDTO(
    @Schema(
        description = "Nome de usuário ou email para login",
        example = "usuario@exemplo.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String username,

    @Schema(
        description = "Senha do usuário",
        example = "S3nhaF0rte!123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "password"
    )
    String password
) {}