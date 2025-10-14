package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para o histórico de vendas da empresa.
 * Estrutura ajustada para corresponder aos dados da entidade HistoricoVenda.
 */
public class HistoricoVendaDTO implements Serializable {
    
    private Long id;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private LocalDateTime dataVenda; 
    private String statusVenda;
    
    // Construtor Padrão
    public HistoricoVendaDTO() {}
    
    // Construtor com 6 campos (ajustado para a ordem do CompanyMapper)
    public HistoricoVendaDTO(Long id, String nomeProduto, Integer quantidade, BigDecimal valorTotal, LocalDateTime dataVenda, String statusVenda) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
        this.statusVenda = statusVenda;
    }

    // ======================
    // Getters e Setters
    // ======================
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public String getStatusVenda() { return statusVenda; }
    public void setStatusVenda(String statusVenda) { this.statusVenda = statusVenda; }
}