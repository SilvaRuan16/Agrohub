package br.com.agrohub.demo.dto; 

import java.io.Serializable;
import java.time.LocalDate; 

public class ClientRegisterRequestDTO implements Serializable {

    // 1. CAMPOS DE USUÁRIO (Tabela: USUARIOS)
    private String email;
    private String senha;
    private String cpf; 

    // 2. CAMPOS DE CLIENTE (Tabela: CLIENTES)
    private String nomeCompleto;
    private String rg;
    private LocalDate dataNascimento;
    
    // ⭐ CAMPOS ADICIONADOS PARA COMPATIBILIDADE COM O FRONT-END E MAPPER ⭐
    private String redeSocial; 
    private String website;
    // ---------------------------------------------------------------------

    // 3. CAMPOS DE CONTATO (Tabela: CONTATOS)
    private String telefone;

    // 4. SUB-DTO DE ENDEREÇO (Tabela: ENDERECOS)
    private EnderecoDTO endereco;

    // Construtor padrão
    public ClientRegisterRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes e services)
    public ClientRegisterRequestDTO(String email, String senha, String cpf, String nomeCompleto, String rg,
            LocalDate dataNascimento, String telefone, EnderecoDTO endereco, String redeSocial, String website) {
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
        // ⭐ Adicionando os novos campos ao construtor
        this.redeSocial = redeSocial; 
        this.website = website;
    }

    // Getters e Setters (Antigos)
    // ... (Seus Getters e Setters para email, senha, cpf, nomeCompleto, rg, dataNascimento, telefone, endereco)

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public EnderecoDTO getEndereco() { return endereco; }
    public void setEndereco(EnderecoDTO endereco) { this.endereco = endereco; }

    // ⭐ NOVO: Getters e Setters para os campos faltantes
    public String getRedeSocial() {
        return redeSocial;
    }

    public void setRedeSocial(String redeSocial) {
        this.redeSocial = redeSocial;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}