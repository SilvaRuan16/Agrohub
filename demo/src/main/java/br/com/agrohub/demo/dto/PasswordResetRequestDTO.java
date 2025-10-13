package br.com.agrohub.demo.dto;

import java.io.Serializable;

public class PasswordResetRequestDTO implements Serializable {

    // 1. Campo de identificação (presente no ForgotPasswordScreen.jsx)
    private String email; 
    
    // 2. O token de segurança enviado ao usuário (para garantir que a requisição é legítima)
    // No seu frontend, o campo 'Código de Verificação' deve mapear para este campo.
    private String tokenVerificacao; 

    // 3. A nova senha que o usuário deseja definir
    private String novaSenha;

    // Construtor padrão
    public PasswordResetRequestDTO() {
    }

    // Construtor com todos os campos
    public PasswordResetRequestDTO(String email, String tokenVerificacao, String novaSenha) {
        this.email = email;
        this.tokenVerificacao = tokenVerificacao;
        this.novaSenha = novaSenha;
    }

    // Getters e Setters
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

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}