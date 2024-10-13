package com.tcc.rebone_3d.DTO;

import com.tcc.rebone_3d.Models.Enum.Perfil;

public record RegisterDTO(String username, String password, Perfil perfil) {
}
