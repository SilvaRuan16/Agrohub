package br.com.agrohub.demo.dto;

import java.io.Serializable;

public class ContatoDTO implements Serializable {
    
    // Campos que mapeiam para a tabela 'contatos' no seu schema SQL
    private String telefone;
    private String email; 
    private String redeSocial; // Campo 'rede_social'
    private String urlSite;    // Campo 'url_site'

    // Construtor padrão
    public ContatoDTO() {
    }

    // Construtor com todos os campos
    public ContatoDTO(String telefone, String email, String redeSocial, String urlSite) {
        this.telefone = telefone;
        this.email = email;
        this.redeSocial = redeSocial;
        this.urlSite = urlSite;
    }

    // Getters e Setters
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRedeSocial() {
        return redeSocial;
    }

    public void setRedeSocial(String redeSocial) {
        this.redeSocial = redeSocial;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }
}