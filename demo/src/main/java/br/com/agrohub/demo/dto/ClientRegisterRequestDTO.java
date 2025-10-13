package br.com.agrohub.demo.dto; // PACOTE CORRETO: user

import java.io.Serializable;
import java.time.LocalDate; // Para data de nascimento

public class ClientRegisterRequestDTO implements Serializable {

    // 1. CAMPOS DE USUÁRIO (Tabela: USUARIOS)
    private String email;
    private String senha;
    private String cpf; // Único, usado para login junto com email

    // 2. CAMPOS DE CLIENTE (Tabela: CLIENTES)
    private String nomeCompleto;
    private String rg;
    private LocalDate dataNascimento;

    // 3. CAMPOS DE CONTATO (Tabela: CONTATOS)
    private String telefone;

    // 4. SUB-DTO DE ENDEREÇO (Tabela: ENDERECOS)
    private EnderecoDTO endereco;

    // Construtor padrão
    public ClientRegisterRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public ClientRegisterRequestDTO(String email, String senha, String cpf, String nomeCompleto, String rg,
            LocalDate dataNascimento, String telefone, EnderecoDTO endereco) {
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }
}