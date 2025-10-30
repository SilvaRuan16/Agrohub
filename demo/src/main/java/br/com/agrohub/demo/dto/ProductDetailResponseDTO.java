package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductDetailResponseDTO implements Serializable {

    // 1. DADOS BÁSICOS DO PRODUTO
    private Long id;
    private String nome;
    private String descricao;
    private String detalhes;
    private BigDecimal precoVenda;
    private String unidadeMedida;
    private Integer quantidadeEstoque;
    private Double descontoMaximo;

    // =========================================================
    // 🚀 CAMPOS ADICIONADOS PARA A DASHBOARD (SEU PEDIDO)
    // =========================================================
    private String codigoInterno;
    private java.math.BigDecimal margemLucro;
    // =========================================================

    // 2. MÍDIA E AVALIAÇÕES
    private List<String> imagensUrls;
    private Double ratingMedio;
    private Integer totalAvaliacoes;
    private List<ComentarioDTO> comentarios;

    // 3. INFORMAÇÕES DA EMPRESA/PRODUTOR
    private CompanyResumeDTO empresa;
    private String municipioEmpresa;
    private String cnpjProdutor;
    private EnderecoDTO enderecoProdutor;

    // 4. INFORMAÇÕES ADICIONAIS/LOGÍSTICAS
    private String categoria;
    private String tipoProduto;
    private String nomeProdutor;
    private String informacaoTecnica;

    // Construtor Padrão
    public ProductDetailResponseDTO() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Double getDescontoMaximo() {
        return descontoMaximo;
    }

    public void setDescontoMaximo(Double descontoMaximo) {
        this.descontoMaximo = descontoMaximo;
    }

    public List<String> getImagensUrls() {
        return imagensUrls;
    }

    public void setImagensUrls(List<String> imagensUrls) {
        this.imagensUrls = imagensUrls;
    }

    public Double getRatingMedio() {
        return ratingMedio;
    }

    public void setRatingMedio(Double ratingMedio) {
        this.ratingMedio = ratingMedio;
    }

    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }

    public CompanyResumeDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CompanyResumeDTO empresa) {
        this.empresa = empresa;
    }

    public String getMunicipioEmpresa() {
        return municipioEmpresa;
    }

    public void setMunicipioEmpresa(String municipioEmpresa) {
        this.municipioEmpresa = municipioEmpresa;
    }

    public String getCnpjProdutor() {
        return cnpjProdutor;
    }

    public void setCnpjProdutor(String cnpjProdutor) {
        this.cnpjProdutor = cnpjProdutor;
    }

    public EnderecoDTO getEnderecoProdutor() {
        return enderecoProdutor;
    }

    public void setEnderecoProdutor(EnderecoDTO enderecoProdutor) {
        this.enderecoProdutor = enderecoProdutor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public String getNomeProdutor() {
        return nomeProdutor;
    }

    public void setNomeProdutor(String nomeProdutor) {
        this.nomeProdutor = nomeProdutor;
    }

    public String getInformacaoTecnica() {
        return informacaoTecnica;
    }

    public void setInformacaoTecnica(String informacaoTecnica) {
        this.informacaoTecnica = informacaoTecnica;
    }

    // =========================================================
    // 🚀 GETTERS E SETTERS ADICIONADOS (SEU PEDIDO)
    // =========================================================
    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public java.math.BigDecimal getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(java.math.BigDecimal margemLucro) {
        this.margemLucro = margemLucro;
    }
    // =========================================================
}