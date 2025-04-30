package com.tcc.rebone_3d.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.rebone_3d.DTO.Usuario.UsuarioBuscaDTO;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public List<UsuarioBuscaDTO> buscarPorNome(String nome) {
        List<Usuario> listaUsuarios = usuarioRepository.findByUsernameContainingIgnoreCase(nome);

        List<UsuarioBuscaDTO> listaDTOUsuarios = listaUsuarios.stream()
        .map(p -> new UsuarioBuscaDTO(p.getUsername(), p.getId()))
        .toList();
        
        return listaDTOUsuarios;
    }
    
}
