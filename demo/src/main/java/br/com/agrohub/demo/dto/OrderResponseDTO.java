package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponseDTO implements Serializable {

    private Long idPedido; // O número/ID único do pedido
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal; // O valor final do pedido
    private String statusPedido; // Ex: "Processando Pagamento", "Confirmado"
    private String formaPagamento; // Nome da forma de pagamento (Ex: "PIX", "Cartão de Crédito")
    
    // Informações de Acompanhamento
    private String linkRastreio; // Opcional, link para rastreio da transportadora
    private String mensagemConfirmacao; // Mensagem amigável para a tela OrderSuccessScreen

    // Construtor padrão
    public OrderResponseDTO() {
    }

    // Construtor com todos os campos
    public OrderResponseDTO(Long idPedido, LocalDateTime dataPedido, BigDecimal valorTotal, String statusPedido, String formaPagamento, String linkRastreio, String mensagemConfirmacao) {
        this.idPedido = idPedido;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.statusPedido = statusPedido;
        this.formaPagamento = formaPagamento;
        this.linkRastreio = linkRastreio;
        this.mensagemConfirmacao = mensagemConfirmacao;
    }

    // Getters e Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getLinkRastreio() {
        return linkRastreio;
    }

    public void setLinkRastreio(String linkRastreio) {
        this.linkRastreio = linkRastreio;
    }

    public String getMensagemConfirmacao() {
        return mensagemConfirmacao;
    }

    public void setMensagemConfirmacao(String mensagemConfirmacao) {
        this.mensagemConfirmacao = mensagemConfirmacao;
    }
}