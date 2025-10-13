package br.com.agrohub.demo.dto; // PACOTE CORRETO: user

import java.io.Serializable;
import java.time.LocalDate; // Para data de fundação

public class CompanyRegisterRequestDTO implements Serializable {

    // 1. CAMPOS DE USUÁRIO (Tabela: USUARIOS)
    private String email;
    private String senha;
    private String cnpj; // Único, usado para login junto com email

    // 2. CAMPOS DE EMPRESA (Tabela: EMPRESAS)
    private String razaoSocial;
    private String nomeFantasia;
    private LocalDate dataFundacao; // Data de abertura da empresa

    // 3. CAMPOS DE CONTATO (Tabela: CONTATOS)
    private String telefone;
    private String urlSite; // Url do site ou rede social

    // 4. SUB-DTO DE ENDEREÇO (Tabela: ENDERECOS)
    private EnderecoDTO endereco;

    // Construtor padrão
    public CompanyRegisterRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public CompanyRegisterRequestDTO(String email, String senha, String cnpj, String razaoSocial, String nomeFantasia,
            LocalDate dataFundacao, String telefone, String urlSite, EnderecoDTO endereco) {
        this.email = email;
        this.senha = senha;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.dataFundacao = dataFundacao;
        this.telefone = telefone;
        this.urlSite = urlSite;
        this.endereco = endereco;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(LocalDate dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }
}