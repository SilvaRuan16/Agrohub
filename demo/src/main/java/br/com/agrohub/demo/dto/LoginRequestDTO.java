// Arquivo: LoginRequestDTO.java
package br.com.agrohub.demo.dto;

import java.io.Serializable;

// NOTA: Renomeei os campos para corresponder EXATAMENTE ao payload do seu React.
public class LoginRequestDTO implements Serializable {

    private String identifier; // Veio do JS
    private String userType;   // Veio do JS
    private String password;   // Veio do JS (Jackson vai mapear para setPassword)

    // Construtor padrão
    public LoginRequestDTO() {
    }

    // Getters e Setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}