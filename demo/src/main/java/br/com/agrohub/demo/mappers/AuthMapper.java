package br.com.agrohub.demo.mappers;

import org.springframework.stereotype.Component; // Ajustado para dto.auth

import br.com.agrohub.demo.dto.TokenResponseDTO;             // Ajustado para models
import br.com.agrohub.demo.models.User;

/**
 * Mapper responsável pela conversão entre a Entidade User e os DTOs de Autenticação.
 */
@Component
public class AuthMapper {

    /**
     * Converte a entidade User para o DTO de resposta de Token.
     */
    public static TokenResponseDTO toTokenResponseDTO(User user, String token) {
        if (user == null) {
            return null;
        }
        
        return TokenResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                // CORREÇÃO: Altere o método de chamada para o nome do Getter correto
                // Se o campo for 'tipoUsuario', o método é getTipoUsuario()
                .userType(user.getTipoUsuario().name()) 
                .token(token)
                .build();
    }
}