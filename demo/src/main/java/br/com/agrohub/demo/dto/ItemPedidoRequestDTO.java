package br.com.agrohub.demo.dto;

import java.io.Serializable;

public class ItemPedidoRequestDTO implements Serializable {
    
    private Long productId;
    private Integer quantidade;

    public ItemPedidoRequestDTO() {}

    public ItemPedidoRequestDTO(Long productId, Integer quantidade) {
        this.productId = productId;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}