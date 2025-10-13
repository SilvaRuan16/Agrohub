package br.com.agrohub.demo.dto; // PACOTE CORRETO: checkout

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

// Não precisa importar CartItemDTO pois está no mesmo pacote (checkout)

public class CheckoutRequestDTO implements Serializable {

    // 1. ITENS DO PEDIDO (Lista de produtos e quantidades)
    private List<CartItemDTO> itens;
    
    // 2. INFORMAÇÕES DE PAGAMENTO (Baseado em ClientCheckoutScreen.jsx)
    private Long formaPagamentoId; // FK para formas_pagamentos (PIX, Cartão, Boleto, etc.)
    private BigDecimal valorTotal; // Opcional para validação no backend
    private String observacoes; // Campo opcional do pedido
    
    // 3. INFORMAÇÕES DE ENTREGA (Baseado no seu schema e fluxo)
    // Opção 1: Usar um endereço já cadastrado
    private Long enderecoEntregaId; 
    
    // Opção 2: Cadastrar um novo endereço para esta entrega
    private EnderecoDTO novoEndereco; 
    
    private Long formaEnvioId; // FK para formas_envios (Ex: Sedex, Retirada, Transportadora)

    // Construtor padrão
    public CheckoutRequestDTO() {
    }

    // Construtor com todos os campos
    public CheckoutRequestDTO(List<CartItemDTO> itens, Long formaPagamentoId, BigDecimal valorTotal, String observacoes, Long enderecoEntregaId, EnderecoDTO novoEndereco, Long formaEnvioId) {
        this.itens = itens;
        this.formaPagamentoId = formaPagamentoId;
        this.valorTotal = valorTotal;
        this.observacoes = observacoes;
        this.enderecoEntregaId = enderecoEntregaId;
        this.novoEndereco = novoEndereco;
        this.formaEnvioId = formaEnvioId;
    }

    // Getters e Setters (Estão corretos e completos)
    public List<CartItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<CartItemDTO> itens) {
        this.itens = itens;
    }

    public Long getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Long formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getEnderecoEntregaId() {
        return enderecoEntregaId;
    }

    public void setEnderecoEntregaId(Long enderecoEntregaId) {
        this.enderecoEntregaId = enderecoEntregaId;
    }

    public EnderecoDTO getNovoEndereco() {
        return novoEndereco;
    }

    public void setNovoEndereco(EnderecoDTO novoEndereco) {
        this.novoEndereco = novoEndereco;
    }

    public Long getFormaEnvioId() {
        return formaEnvioId;
    }

    public void setFormaEnvioId(Long formaEnvioId) {
        this.formaEnvioId = formaEnvioId;
    }
}