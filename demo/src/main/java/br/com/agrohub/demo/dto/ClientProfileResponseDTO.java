package br.com.agrohub.demo.dto; // PACOTE CORRETO

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ClientProfileResponseDTO implements Serializable {

    // 1. DADOS DE USUÁRIO E CLIENTE (ClientProfileScreen.jsx)
    private Long id; // ID do Cliente
    private String email;
    private String nomeCompleto;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String telefone; // Contato principal

    // 2. ENDEREÇOS CADASTRADOS (Para seleção na tela de perfil/checkout)
    private List<EnderecoDTO> enderecos;

    // 3. HISTÓRICO DE COMPRAS (Mapeamento do 'historyMock' no
    // ClientProfileScreen.jsx)
    private List<HistoricoPedidoDTO> historicoPedidos;

    // Construtor Padrão
    public ClientProfileResponseDTO() {
    }

    // Construtor com todos os campos (recomendado)
    public ClientProfileResponseDTO(Long id, String email, String nomeCompleto, String cpf, String rg,
            LocalDate dataNascimento, String telefone, List<EnderecoDTO> enderecos,
            List<HistoricoPedidoDTO> historicoPedidos) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.enderecos = enderecos;
        this.historicoPedidos = historicoPedidos;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }

    public List<HistoricoPedidoDTO> getHistoricoPedidos() {
        return historicoPedidos;
    }

    public void setHistoricoPedidos(List<HistoricoPedidoDTO> historicoPedidos) {
        this.historicoPedidos = historicoPedidos;
    }
}