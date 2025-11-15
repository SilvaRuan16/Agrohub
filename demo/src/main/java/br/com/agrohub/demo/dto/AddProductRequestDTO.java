package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty; // NOVO: ESSENCIAL PARA O MAPEAMENTO
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para o cadastro de um novo produto.
 * Mapeia JSON em português para variáveis DTO em inglês usando @JsonProperty.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequestDTO implements Serializable {

    // ===================================
    // MAPEAMENTO: JSON (Português) -> Variável (Inglês)
    // ===================================
    @JsonProperty("nome")
    private String name; // Mapeia o campo "nome" do JSON para a variável 'name'.

    @JsonProperty("descricao")
    private String shortDescription; // Mapeia o campo "descricao" do JSON.

    @JsonProperty("precoVenda")
    private BigDecimal salePrice; // Mapeia o campo "precoVenda" do JSON.

    @JsonProperty("quantidadeEstoque")
    private Integer initialStock; // Mapeia o campo "quantidadeEstoque" do JSON.

    @JsonProperty("unidadeMedida")
    private String unitOfMeasurement; // Campo obrigatório no FE/DB.

    @JsonProperty("tipoProdutoId")
    private Long productTypeId; // Mapeia o campo "tipoProdutoId" do JSON.

    @JsonProperty("descontoId")
    private Long discountId; // Mapeia o campo "descontoId" do JSON.

    // ===================================
    // Campos adicionais do FE (também em português no @JsonProperty)
    // ===================================
    @JsonProperty("produtor")
    private String produtorName; // Mapeia "Informação Adicional" do FE para 'produtor'.

    @JsonProperty("linkAdicional")
    private String link; // Mapeia o campo "linkAdicional" (link) do JSON.

    // Campos que o FE não envia (deixados para compatibilidade, podem ser nulos)
    private String detailedDescription;
    private BigDecimal costPrice;
    private Integer minStock;
    private String municipality;
    private String producerCnpj;
    private EnderecoDTO producerAddress;
}