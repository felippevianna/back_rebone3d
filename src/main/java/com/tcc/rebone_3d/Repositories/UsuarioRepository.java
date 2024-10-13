package com.tcc.rebone_3d.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.tcc.rebone_3d.Models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  UserDetails findByUsername(String login);
}
