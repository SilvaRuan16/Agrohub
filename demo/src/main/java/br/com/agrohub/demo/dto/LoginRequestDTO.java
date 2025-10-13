package br.com.agrohub.demo.dto;

import java.io.Serializable;

public class LoginRequestDTO implements Serializable {

    // O usuário pode logar com e-mail ou com CPF/CNPJ (dependendo da sua regra de negócio),
    // mas o e-mail é o campo mais universal e foi a base do seu formulário.
    private String email;
    private String senha;

    // Construtor padrão
    public LoginRequestDTO() {
    }

    // Construtor com todos os campos
    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}