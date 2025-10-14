package br.com.agrohub.demo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um registro de venda associado a uma empresa.
 * Simula um pedido concluído para fins de histórico e é usada pelo CompanyMapper
 * para gerar o DTO de Histórico de Vendas.
 */
@Entity
@Table(name = "historico_vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Usado pelo CompanyMapper

    // Relacionamento com a empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Company company; 

    @Column(name = "nome_produto", nullable = false, length = 150)
    private String nomeProduto; // Usado pelo CompanyMapper

    @Column(nullable = false)
    private Integer quantidade; // Usado pelo CompanyMapper (quantidade total vendida)

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal; // Usado pelo CompanyMapper

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda; // Usado pelo CompanyMapper

    @Column(name = "status_venda", nullable = false, length = 50)
    private String statusVenda; // Usado pelo CompanyMapper (Ex: Concluída, Enviada)
}
