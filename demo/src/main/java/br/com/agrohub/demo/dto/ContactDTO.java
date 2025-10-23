package br.com.agrohub.demo.dto;

import java.io.Serializable;

// DTO para receber os dados aninhados do campo 'contact' do Front-end
public class ContactDTO implements Serializable {
    
    // ATENÇÃO: Os nomes devem ser os mesmos usados no Front-end (RegisterClientScreen.js)
    
    private String email;
    private String telefone;
    private String redeSocial;

    // O Front-end envia 'website'. Na Entidade Contact, o campo é 'urlSite'. 
    // Seu Mapper/Service precisará fazer essa conversão.
    private String website; 

    // Construtor padrão
    public ContactDTO() {}

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getRedeSocial() { return redeSocial; }
    public void setRedeSocial(String redeSocial) { this.redeSocial = redeSocial; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}