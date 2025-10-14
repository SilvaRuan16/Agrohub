package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para o cadastro de um novo produto.
 * Contém dados do produto e informações adicionais.
 * * Este DTO resolve a ausência da classe no ProductMapper.
 */
@Data // Gera Getters, Setters, toString, equals e hashCode
@Builder // Útil para a criação de objetos
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class AddProductRequestDTO implements Serializable {

    // ===================================
    // DADOS DO PRODUTO (Mapeados para Product.java)
    // ===================================
    private String name;                // Mapeia para product.nome
    private String shortDescription;    // Mapeia para product.descricaoCurta
    private String detailedDescription; // Mapeia para product.descricaoDetalhada
    private BigDecimal salePrice;       // Mapeia para product.precoVenda
    private BigDecimal costPrice;       // Mapeia para product.precoCusto
    private Integer minStock;           // Mapeia para product.estoqueMinimo
    private Integer initialStock;       // Mapeia para product.estoqueAtual
    private String unitOfMeasurement;   // Mapeia para product.unidadeMedida

    // ===================================
    // DADOS DE INFORMAÇÕES ADICIONAIS (Mapeados para AdditionalInfo.java)
    // ===================================
    private String produtorName;        // Mapeia para info.produtor
    private String municipality;        // Mapeia para info.municipio
    private String producerCnpj;        // Mapeia para info.cnpjProdutor
    
    // ID para buscar o ProductType (tipo_produto_id)
    private Long productTypeId; 
    
    // DTO de endereço para mapear a localização do produtor/produto
    private EnderecoDTO producerAddress; 
}