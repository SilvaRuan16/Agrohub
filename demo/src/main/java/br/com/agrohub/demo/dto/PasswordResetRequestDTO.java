package br.com.agrohub.demo.dto;

import java.io.Serializable;
// Removendo imports e anotações do Lombok
// import jakarta.validation.constraints.Email; 
// import jakarta.validation.constraints.NotBlank;
// AVISO: Se você remover as anotações @Email e @NotBlank, a validação não funcionará.

public class PasswordResetRequestDTO implements Serializable {

    // 1. Campo de identificação
    private String email; 
    
    // 2. O token de segurança (Código de Verificação)
    private String tokenVerificacao; 

    // 3. A nova senha. (RENOMEADO para newPassword para o AuthService compilar)
    private String newPassword;

    // Construtor padrão
    public PasswordResetRequestDTO() {
    }

    // Construtor com todos os campos
    public PasswordResetRequestDTO(String email, String tokenVerificacao, String newPassword) {
        this.email = email;
        this.tokenVerificacao = tokenVerificacao;
        this.newPassword = newPassword;
    }

    // Getters e Setters (Gerados manualmente)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenVerificacao() {
        return tokenVerificacao;
    }

    public void setTokenVerificacao(String tokenVerificacao) {
        this.tokenVerificacao = tokenVerificacao;
    }

    // Método que o AuthService estava esperando
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}