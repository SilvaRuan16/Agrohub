package br.com.agrohub.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define o encoder de senhas. Usamos o BCrypt, padrão e seguro.
     * Este Bean será injetado em classes como AuthService.java.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura as regras de segurança HTTP e as permissões de acesso.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita CSRF para permitir POST/PUT/DELETE sem tokens de formulário (comum em APIs REST)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Configura as regras de autorização para endpoints (URLs)
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints públicos: Login, Registro, Redefinir Senha
                // (Referentes às telas p1.png, p2.png, p3.png)
                .requestMatchers("/api/auth/**").permitAll() 
                
                // Endpoints de catálogo e busca (h1.png, h2.png) - Podem ser acessados sem login
                .requestMatchers("/api/products/public/**", "/api/companies/public/**").permitAll()
                
                // Todos os outros endpoints exigem autenticação (usuário logado)
                .anyRequest().authenticated()
            )
            
            // 3. Configura a autenticação básica HTTP (opcional para testes simples de API)
            // Em uma aplicação real, aqui entrará a configuração de filtros JWT
            .httpBasic(withDefaults());

        return http.build();
    }
}