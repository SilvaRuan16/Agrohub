package br.com.agrohub.demo.dto;

import java.io.Serializable;

public class AuthResponseDTO implements Serializable {

    private String token; // O token JWT para futuras requisições autenticadas
    private String tipoUsuario; // Ex: "CLIENTE" ou "EMPRESA" (para redirecionamento no front)
    private Long usuarioId; // ID primário do usuário
    private String nome; // Nome Completo ou Nome Fantasia/Razão Social

    // Construtor padrão
    public AuthResponseDTO() {
    }

    // Construtor com todos os campos
    public AuthResponseDTO(String token, String tipoUsuario, Long usuarioId, String nome) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.usuarioId = usuarioId;
        this.nome = nome;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}