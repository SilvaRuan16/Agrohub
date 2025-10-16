package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.util.List;

public class OrderRequestDTO implements Serializable {
    
    private Long clientId;
    private List<ItemPedidoRequestDTO> items;
    private String paymentMethod; // Ex: PIX, CARTAO, BOLETO
    private Long addressId; // Endereço de entrega (simulação)

    public OrderRequestDTO() {}

    public OrderRequestDTO(Long clientId, List<ItemPedidoRequestDTO> items, String paymentMethod, Long addressId) {
        this.clientId = clientId;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.addressId = addressId;
    }

    // Getters e Setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public List<ItemPedidoRequestDTO> getItems() { return items; }
    public void setItems(List<ItemPedidoRequestDTO> items) { this.items = items; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
}