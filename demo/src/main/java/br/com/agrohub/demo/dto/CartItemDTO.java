package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItemDTO implements Serializable {

    // Identifica qual produto está no carrinho
    private Long idProduto;

    // A quantidade desejada pelo cliente
    private Integer quantidade;

    // Preço unitário do produto NO MOMENTO da compra.
    // É uma boa prática enviar isso para validação no backend,
    // mas o backend deve sempre consultar o preço atual no banco.
    private BigDecimal precoUnitario;

    // Construtor padrão
    public CartItemDTO() {
    }

    // Construtor com todos os campos
    public CartItemDTO(Long idProduto, Integer quantidade, BigDecimal precoUnitario) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}