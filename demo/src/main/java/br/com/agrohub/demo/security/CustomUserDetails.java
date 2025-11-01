package br.com.agrohub.demo.security;

import br.com.agrohub.demo.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Esta classe atua como um wrapper para sua entidade User, implementando a interface UserDetails
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeia o seu UserType (ex: EMPRESA, CLIENTE) para a Role do Spring Security.
        // O prefixo "ROLE_" é uma boa prática.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario().name()));
    }

    @Override
    public String getPassword() {
        // Retorna a senha hasheada do seu objeto User.
        return user.getSenha(); 
    }

    @Override
    public String getUsername() {
        // Retorna o nome de usuário (identifier, CPF/CNPJ/Email) usado para login.
        return user.getCpf() != null ? user.getCpf() : (user.getCnpj() != null ? user.getCnpj() : user.getEmail());
    }

    // Métodos de expiração e bloqueio (geralmente retornam true)
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