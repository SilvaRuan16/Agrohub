// SEU DTO JÁ ESTÁ CORRETO
package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para exibição de produtos em lista/card (Dashboard do Cliente).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor // Necessário para o construtor usado no ProductMapper
public class ProductCardResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private BigDecimal precoVenda;
    private String unidadeMedida; // Campo corrigido para o DTO
    private List<String> imagensUrls;
    private Double ratingMedio;
    private Integer totalAvaliacoes;
    private CompanyResumeDTO empresa; 
    
}