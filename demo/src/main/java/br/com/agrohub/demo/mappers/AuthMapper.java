package br.com.agrohub.demo.mappers;

import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.TokenResponseDTO;
import br.com.agrohub.demo.models.User;

@Component
public class AuthMapper {

    public static TokenResponseDTO toTokenResponseDTO(User user, String token) {
        if (user == null) {
            return null;
        }
        
        return TokenResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                // CORREÇÃO: Usando getTipoUsuario() em vez de getUserType()
                .userType(user.getTipoUsuario().name()) 
                .token(token)
                .build();
    }
}
