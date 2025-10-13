package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductListResponseDTO implements Serializable {

    // 1. DADOS DE IDENTIFICAÇÃO E NOME (ClientDashboardScreen.jsx)
    private Long idProduto; // Usado para navegação para a tela de detalhes
    private String nome;

    // 2. DADOS DE PREÇO E AVALIAÇÃO
    private BigDecimal precoVenda;
    private String unidadeMedida; // Ex: /kg, /un, /saca (para exibição no preço)
    private Double ratingMedio; // Campo 'rating' no mock (avaliação média)
    
    // 3. MÍDIA
    private String imagemPrincipalUrl; // Campo 'image' no mock (apenas a URL da primeira imagem)

    // Construtor padrão
    public ProductListResponseDTO() {
    }

    // Construtor com todos os campos
    public ProductListResponseDTO(Long idProduto, String nome, BigDecimal precoVenda, String unidadeMedida, Double ratingMedio, String imagemPrincipalUrl) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.precoVenda = precoVenda;
        this.unidadeMedida = unidadeMedida;
        this.ratingMedio = ratingMedio;
        this.imagemPrincipalUrl = imagemPrincipalUrl;
    }

    // Getters e Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Double getRatingMedio() {
        return ratingMedio;
    }

    public void setRatingMedio(Double ratingMedio) {
        this.ratingMedio = ratingMedio;
    }

    public String getImagemPrincipalUrl() {
        return imagemPrincipalUrl;
    }

    public void setImagemPrincipalUrl(String imagemPrincipalUrl) {
        this.imagemPrincipalUrl = imagemPrincipalUrl;
    }
}