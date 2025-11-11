package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors; // <-- Import já existente, necessário para o novo método

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

    /**
     * Mapeia a entidade Product para o DTO de Detalhe (usado para o Dashboard da
     * Empresa).
     * * @param product Entidade Produto completa.
     * * @return ProductDetailResponseDTO.
     */
    public ProductDetailResponseDTO toProductDetailDTO(Product product) {
        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();

        // 1. DADOS PRINCIPAIS (Product)
        dto.setId(product.getId());
        dto.setNome(product.getNome());

        // CAMPOS ADICIONADOS PARA A DASHBOARD
        dto.setCodigoInterno(product.getCodigoInterno());
        dto.setMargemLucro(product.getMargemLucro());

        dto.setPrecoVenda(product.getPrecoVenda());
        dto.setUnidadeMedida(product.getUnitOfMeasurement());
        dto.setQuantidadeEstoque(product.getQuantidadeEstoque());
        dto.setDescricao(product.getDescricao());

        // 2. MÍDIA
        List<String> imageUrls = product.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
        dto.setImagensUrls(imageUrls);

        // 3. DESCONTO
        Discount discount = product.getDiscount();
        if (discount != null) {
            dto.setDescontoMaximo(discount.getPercentual().doubleValue());
        }

        // 4. AVALIAÇÕES
        dto.setRatingMedio(4.5);
        dto.setTotalAvaliacoes(50);

        List<ComentarioDTO> comentariosDTO = product.getComments().stream()
                .map(commonMapper::toComentarioDTO)
                .collect(Collectors.toList());
        dto.setComentarios(comentariosDTO);

        // 5. INFORMAÇÕES DA EMPRESA (COMPANY)
        Company company = product.getCompany();
        if (company != null) {
            CompanyResumeDTO companyDto = new CompanyResumeDTO(company.getId(), company.getNomeFantasia());
            dto.setEmpresa(companyDto);
        }

        // 6. INFORMAÇÕES ADICIONAIS
        AdditionalInfo info = product.getAdditionalInfo();
        if (info != null) {
            dto.setNomeProdutor(info.getProdutor());
            dto.setCnpjProdutor(info.getCnpjProdutor());
            dto.setMunicipioEmpresa(info.getMunicipio());

            ProductType productType = info.getProductType();
            if (productType != null) {
                dto.setTipoProduto(productType.getTipo());
            }
        }

        return dto;
    }

    // =========================================================================
    // 🎯 NOVO MÉTODO ADICIONADO (Para corrigir os erros de teste)
    // =========================================================================

    /**
     * Mapeia uma lista de entidades Product para uma lista de DTOs de Detalhe.
     * 
     * @param products Lista de entidades Produto.
     * @return Lista de ProductDetailResponseDTO.
     */
    public List<ProductDetailResponseDTO> toProductDetailDTOList(List<Product> products) {
        if (products == null) {
            return List.of(); // Retorna lista vazia se a entrada for nula
        }
        return products.stream()
                .map(this::toProductDetailDTO) // Reutiliza o método de mapeamento individual
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

    /**
     * Mapeia o DTO de Requisição para a Entidade Product.
     * * @param dto     AddProductRequestDTO.
     * * @param company Empresa associada.
     * 
     * @return Entidade Product.
     */
    public Product toProductEntity(AddProductRequestDTO dto, Company company) {
        // [Mantenha seu código original aqui]
        Product product = new Product();
        product.setCompany(company);
        product.setNome(dto.getName());
        product.setDescricao(dto.getShortDescription());
        // ... set outros campos
        return product; // Placeholder
    }
}