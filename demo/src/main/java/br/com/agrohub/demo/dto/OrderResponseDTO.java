package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO de resposta para o login bem-sucedido.
 * Contém o ID do pedido, valor final e status.
 */
public class OrderResponseDTO implements Serializable {
    
    private Long orderId;
    private BigDecimal totalAmount;
    private String status;

    // Construtor 1: Construtor padrão (sem argumentos)
    public OrderResponseDTO() {
    }

    // CONSTRUTOR 2: Construtor de 3 argumentos (ID, VALOR, STATUS)
    // Este construtor resolve o erro de compilação no OrderService.
    public OrderResponseDTO(Long orderId, BigDecimal totalAmount, String status) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
    }
    
    // Getters e Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}