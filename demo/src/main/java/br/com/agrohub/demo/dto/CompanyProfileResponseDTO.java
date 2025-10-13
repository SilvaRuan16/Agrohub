package br.com.agrohub.demo.dto; // PACOTE CORRETO: user

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class CompanyProfileResponseDTO implements Serializable {

    // 1. DADOS DE USUÁRIO E EMPRESA (CompanyProfileScreen.jsx)
    private Long id; // ID da Empresa
    private String email;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private LocalDate dataFundacao; // Data de abertura da empresa
    private String telefone;
    private String urlSite;
    private String logoUrl; // URL da logo da empresa

    // 2. ENDEREÇOS CADASTRADOS
    private List<EnderecoDTO> enderecos;

    // 3. RESUMO DO ESTOQUE/VENDAS (Similar ao histórico do cliente)
    private List<HistoricoVendaDTO> historicoVendas;

    // Construtor Padrão
    public CompanyProfileResponseDTO() {
    }

    // Construtor com todos os campos (implementado)
    public CompanyProfileResponseDTO(Long id, String email, String razaoSocial, String nomeFantasia, String cnpj,
            LocalDate dataFundacao, String telefone, String urlSite, String logoUrl, List<EnderecoDTO> enderecos,
            List<HistoricoVendaDTO> historicoVendas) {
        this.id = id;
        this.email = email;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.dataFundacao = dataFundacao;
        this.telefone = telefone;
        this.urlSite = urlSite;
        this.logoUrl = logoUrl;
        this.enderecos = enderecos;
        this.historicoVendas = historicoVendas;
    }

    // Getters e Setters (Estão corretos)
    // ...
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }

    public List<HistoricoVendaDTO> getHistoricoVendas() {
        return historicoVendas;
    }

    public void setHistoricoVendas(List<HistoricoVendaDTO> historicoVendas) {
        this.historicoVendas = historicoVendas;
    }
}