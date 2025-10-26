package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class CompanyRegisterRequestDTO implements Serializable {

    private String email;
    private String senha;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private LocalDate dataFundacao;
    private String telefone;
    private String urlSite;
    private ContactDTO contact;
    private EnderecoDTO endereco;

    // Construtor padrão
    public CompanyRegisterRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public CompanyRegisterRequestDTO(String email, String senha, String cnpj, String razaoSocial, String nomeFantasia,
            LocalDate dataFundacao, String telefone, String urlSite, ContactDTO contact, EnderecoDTO endereco) {
        this.email = email;
        this.senha = senha;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.dataFundacao = dataFundacao;
        this.telefone = telefone;
        this.urlSite = urlSite;
        this.contact = contact;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public LocalDate getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(LocalDate dataFundacao) { this.dataFundacao = dataFundacao; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getUrlSite() { return urlSite; }
    public void setUrlSite(String urlSite) { this.urlSite = urlSite; }
    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }

    public ContactDTO getContact() { return contact; }
    public void setContact(ContactDTO contact) { this.contact = contact; }
}