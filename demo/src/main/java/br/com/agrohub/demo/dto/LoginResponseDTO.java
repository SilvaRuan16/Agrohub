package br.com.agrohub.demo.dto;

import java.io.Serializable;

/**
 * DTO de resposta para o login bem-sucedido.
 * Contém o token JWT, uma mensagem e o tipo de usuário.
 */
public class LoginResponseDTO implements Serializable {
    
    private String token;
    private String message;
    private String userType; // Ex: CLIENTE, EMPRESA
    
    // Construtor padrão
    public LoginResponseDTO() {
    }

    // Construtor com todos os campos
    public LoginResponseDTO(String token, String message, String userType) {
        this.token = token;
        this.message = message;
        this.userType = userType;
    }
    
    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}