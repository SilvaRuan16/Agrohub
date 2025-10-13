package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricoVendaDTO implements Serializable {
    
    private Long idVenda; // id do pedido/venda
    private String nomeCliente; // nome do comprador
    private String itemResumo; // Ex: "4 itens" ou "Saca de Café"
    private Integer quantidadeProdutos; // Total de produtos vendidos (unidades)
    private BigDecimal valorVenda; // Valor total da venda
    private LocalDateTime dataVenda; 
    private String statusEntrega; // Ex: "Aguardando Envio", "Entregue"
    
    // Construtor Padrão
    public HistoricoVendaDTO() {}
    
    // Construtor com todos os campos
    public HistoricoVendaDTO(Long idVenda, String nomeCliente, String itemResumo, Integer quantidadeProdutos, BigDecimal valorVenda, LocalDateTime dataVenda, String statusEntrega) {
        this.idVenda = idVenda;
        this.nomeCliente = nomeCliente;
        this.itemResumo = itemResumo;
        this.quantidadeProdutos = quantidadeProdutos;
        this.valorVenda = valorVenda;
        this.dataVenda = dataVenda;
        this.statusEntrega = statusEntrega;
    }

    // Getters e Setters
    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getItemResumo() { return itemResumo; }
    public void setItemResumo(String itemResumo) { this.itemResumo = itemResumo; }
    public Integer getQuantidadeProdutos() { return quantidadeProdutos; }
    public void setQuantidadeProdutos(Integer quantidadeProdutos) { this.quantidadeProdutos = quantidadeProdutos; }
    public BigDecimal getValorVenda() { return valorVenda; }
    public void setValorVenda(BigDecimal valorVenda) { this.valorVenda = valorVenda; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
    public String getStatusEntrega() { return statusEntrega; }
    public void setStatusEntrega(String statusEntrega) { this.statusEntrega = statusEntrega; }
}