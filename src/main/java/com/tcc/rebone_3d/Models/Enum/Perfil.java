package com.tcc.rebone_3d.Models.Enum;

public enum Perfil {
    ADMIN("admin"),
    DENTISTA("dentista"),
    PROTETICO("protetico");

    private String perfil;

    Perfil(String perfil){
        this.perfil = perfil;
    }

    public String getPerfil(){
        return perfil;
    }
}

