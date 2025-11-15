package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.AddProductRequestDTO;
import br.com.agrohub.demo.dto.ComentarioDTO;
import br.com.agrohub.demo.dto.CompanyResumeDTO;
import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
import br.com.agrohub.demo.models.AdditionalInfo;
import br.com.agrohub.demo.models.Comment;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Discount;
import br.com.agrohub.demo.models.Image;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.models.ProductType;

/**
 * Mapper responsável pela conversão entre a Entidade Product e seus DTOs de
 * Card, Detalhe e Requisição.
 */
@Component
public class ProductMapper {

    private final CommonMapper commonMapper;

    public ProductMapper(@Lazy CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    // --- Métodos de Mapeamento de Resposta (Mantidos como Placeholder) ---

    /**
     * Mapeia a entidade Product para o DTO de Detalhe.
     * * @param product Entidade Produto.
     * * @return ProductDetailResponseDTO.
     */
    public ProductDetailResponseDTO toProductDetailDTO(Product product) {
        // [Mantenha seu código original aqui]
        return new ProductDetailResponseDTO(); // Placeholder
    }

    /**
     * Mapeia uma lista de entidades Produto para uma lista de DTOs de Detalhe.
     * * @param products Lista de entidades Produto.
     * * @return Lista de ProductDetailResponseDTO.
     */
    public List<ProductDetailResponseDTO> toProductDetailDTOList(List<Product> products) {
        if (products == null) {
            return List.of(); // Retorna lista vazia se a entrada for nula
        }
        return products.stream()
                .map(this::toProductDetailDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapeia a entidade Product para o DTO de Card (usado para o Dashboard do
     * Cliente).
     * * @param product Entidade Produto.
     * * @return ProductCardResponseDTO.
     */
    public ProductCardResponseDTO toProductCardDTO(Product product) {
        // [Mantenha seu código original aqui]
        return new ProductCardResponseDTO(); // Placeholder
    }

    // --- Método de Mapeamento de Requisição (CORRIGIDO) ---

    /**
     * Mapeia o DTO de Requisição (AddProductRequestDTO) para a Entidade Product.
     * * @param dto AddProductRequestDTO.
     * * @param company Empresa associada.
     * * @return Entidade Product.
     */
    public Product toProductEntity(AddProductRequestDTO dto, Company company) {
        Product product = new Product();

        // 1. Mapeamento de Entidades
        product.setCompany(company);

        // 2. Mapeamento dos Dados do Produto (DB/Português <- DTO/Inglês)
        // Usamos os getters das variáveis em inglês do DTO.

        product.setNome(dto.getName());
        product.setDescricao(dto.getShortDescription());
        product.setPrecoVenda(dto.getSalePrice());
        product.setQuantidadeEstoque(dto.getInitialStock());

        // 🎯 CORREÇÃO CRÍTICA: Mapeamento da unidade de medida que estava faltando.
        product.setUnitOfMeasurement(dto.getUnitOfMeasurement());

        // Mapeamento dos campos opcionais (Se eles existirem na sua Entidade Product)
        // Se a entidade Product tiver estes setters:

        // Mapeamento de IDs (ajuste o nome do setter conforme sua Entidade)
        // product.setTipoProdutoId(dto.getProductTypeId());
        // product.setDescontoId(dto.getDiscountId());

        // Mapeamento de Informações Adicionais (Se estiverem na Entidade Product)
        // product.setLinkAdicional(dto.getLinkAdicional());

        // Se o DTO tiver um campo 'detailedDescription' e a entidade tiver o setter
        // correspondente
        // product.setDescricaoDetalhada(dto.getDetailedDescription());

        return product;
    }
}