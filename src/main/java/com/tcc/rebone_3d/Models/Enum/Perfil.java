package com.tcc.rebone_3d.Models.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Perfil",
    description = "Tipos de perfis de usuário no sistema Rebone3D",
    enumAsRef = true
)
public enum Perfil {
    @Schema(
        description = "Administrador do sistema com acesso completo",
        example = "admin"
    )
    ADMIN("admin"),

    @Schema(
        description = "Dentista responsável por procedimentos odontológicos",
        example = "dentista"
    )
    DENTISTA("dentista"),

    @Schema(
        description = "Protético responsável pela confecção de próteses",
        example = "protetico"
    )
    PROTETICO("protetico");

    @Schema(
        description = "Valor textual do perfil",
        example = "dentista"
        // hidden = true // Opcional: esconde este campo na documentação
    )
    private String perfil;

    Perfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}