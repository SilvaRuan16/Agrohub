package br.com.agrohub.demo.dto; // PACOTE CORRETO: dto.auth

import java.io.Serializable; // Usaremos Lombok @Builder para compatibilidade com o AuthService

import lombok.Builder;   // Inclui Getters, Setters, toString, equals, hashCode
import lombok.Data;

/**
 * DTO de resposta para o processo de autenticação (login).
 * Contém o token JWT e as informações básicas do usuário logado.
 * Implementa @Data e @Builder, assumindo o uso da biblioteca Lombok.
 */
@Data
@Builder
public class TokenResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String email;
    private String userType; // Ex: "CLIENTE" ou "EMPRESA"
    private String token; // O token JWT (ou simulado)

    // Se você não usa Lombok, adicione manualmente:
    
    // Construtor Padrão
    public TokenResponseDTO() {
    }

    // Construtor Completo
    public TokenResponseDTO(Long id, String email, String userType, String token) {
        this.id = id;
        this.email = email;
        this.userType = userType;
        this.token = token;
    }
    
    // Getters e Setters (omitidos aqui porque o @Data os gera, mas seriam necessários sem Lombok)
}