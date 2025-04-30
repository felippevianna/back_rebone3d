package com.tcc.rebone_3d.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.rebone_3d.DTO.AuthenticationDTO;
import com.tcc.rebone_3d.DTO.LoginResponseDTO;
import com.tcc.rebone_3d.DTO.RegisterDTO;
import com.tcc.rebone_3d.DTO.Usuario.UsuarioInfoDTO;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.UsuarioRepository;
import com.tcc.rebone_3d.Security.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Cadastro e Login de usuário")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login de Usuário", description = "Realiza o login do usuário")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastro de Usuário", description = "Realiza o cadastro de um novo usuário")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByUsername(data.username()) != null)
            return ResponseEntity.badRequest().build();

        // TODO: adicionar email ao campo de usuário 
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        
        // Cria um novo usuário
        Usuario newUser = new Usuario();
        newUser.setUsername(data.username());
        newUser.setPassword(encryptedPassword);
        newUser.setPerfil(data.perfil());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    // Novo endpoint para obter informações do usuário
    @GetMapping("/user-info")
    @Operation(summary = "Obter informações do usuário logado", description = "Retorna as informações adicionais do usuário logado - ainda não está finalizada")
    public ResponseEntity<UsuarioInfoDTO> userInfo(Authentication authentication) {
        // Obtém o usuário autenticado a partir do contexto de segurança
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Cria um DTO com as informações do usuário
        UsuarioInfoDTO userInfo = new UsuarioInfoDTO(usuario.getUsername(), usuario.getPerfil());

        return ResponseEntity.ok(userInfo);
    }
}
