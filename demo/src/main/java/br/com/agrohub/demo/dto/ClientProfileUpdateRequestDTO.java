package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class ClientProfileUpdateRequestDTO implements Serializable {

    // Campos que mapeiam para a tabela CLIENTES
    private String nomeCompleto;
    private LocalDate dataNascimento;

    // Campos que mapeiam para a tabela CONTATOS
    private String telefone;

    // Campos que mapeiam para a tabela USUARIOS (se a senha for alterada)
    // Usamos 'novaSenha' para clareza e para evitar confusão com a senha atual
    private String novaSenha; // Opcional

    // Construtor padrão
    public ClientProfileUpdateRequestDTO() {
    }

    // Construtor com todos os campos (útil para testes)
    public ClientProfileUpdateRequestDTO(String nomeCompleto, LocalDate dataNascimento, String telefone,
            String novaSenha) {
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.novaSenha = novaSenha;
    }

    // Getters e Setters
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
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

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}