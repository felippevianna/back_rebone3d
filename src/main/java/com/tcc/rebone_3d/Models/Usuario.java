package com.tcc.rebone_3d.Models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcc.rebone_3d.Models.Enum.Perfil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    
    @JsonIgnore
    private String password;
    
    private Perfil perfil;

    // Construtor com parâmetros
    public Usuario(String nome, String senha, Perfil perfil) {
        this.username = nome;
        this.password = senha;
        this.perfil = perfil;
    }

    
    @OneToMany(mappedBy = "usuarioResponsavel", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Paciente> pacientes;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Imagem> imagens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.perfil == Perfil.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), 
                new SimpleGrantedAuthority("ROLE_DENTISTA"), 
                new SimpleGrantedAuthority("ROLE_PROTETICO"));
        } else if (this.perfil == Perfil.DENTISTA) {
            return List.of(new SimpleGrantedAuthority("ROLE_DENTISTA"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_PROTETICO"));
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
