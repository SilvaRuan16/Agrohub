package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricoPedidoDTO implements Serializable {
    
    private Long idPedido; // id
    private String item; // nome do item ou resumo do pedido
    private Integer quantity; // quantidade total de itens diferentes
    private BigDecimal price; // valor total do pedido
    private LocalDateTime date; // data da compra
    private String status; // status do pedido (Ex: Entregue, Enviado, Cancelado)
    
    // Construtor Padrão
    public HistoricoPedidoDTO() {}
    
    // Construtor com todos os campos
    public HistoricoPedidoDTO(Long idPedido, String item, Integer quantity, BigDecimal price, LocalDateTime date, String status) {
        this.idPedido = idPedido;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    // Getters e Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}