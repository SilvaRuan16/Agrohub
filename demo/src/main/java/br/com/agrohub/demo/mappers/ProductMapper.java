package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

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

    // Se CommonMapper não existe, o projeto não irá compilar.
    // Presumindo que ele exista, mas com métodos incorretos ou ausentes.
    private final CommonMapper commonMapper;

    // Você precisará de um CommonMapper (que não foi fornecido, mas é essencial
    // para mapear Endereço, Comentários, etc.)
    public ProductMapper(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    /**
     * Mapeia a entidade Product para o DTO de Detalhe (usado para o Dashboard da
     * Empresa).
     * * @param product Entidade Produto completa.
     * 
     * @return ProductDetailResponseDTO.
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
            // CORREÇÃO: Converte BigDecimal para Double, resolvendo a incompatibilidade de
            // tipos (e usando getPercentual())
            dto.setDescontoMaximo(discount.getPercentual().doubleValue());
        }

        // 4. AVALIAÇÕES
        dto.setRatingMedio(4.5);
        dto.setTotalAvaliacoes(50);

        // CORREÇÃO: Bloco de Mapeamento de comentários REATIVADO
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

            // Mapeamento de endereço do produtor (Assumindo que está no CommonMapper)
            // dto.setEnderecoProdutor(commonMapper.toEnderecoDTO(info.getAddress()));
        }

        return dto;
    }

    /**
     * Mapeia a entidade Product para o DTO de Card (usado para o Dashboard do
     * Cliente).
     * * @param product Entidade Produto.
     * 
     * @return ProductCardResponseDTO.
     */
    public ProductCardResponseDTO toProductCardDTO(Product product) {
        // ... (seu código existente para toProductCardDTO)
        // [CÓDIGO OMITIDO POR SER INALTERADO]
        return new ProductCardResponseDTO(); // Placeholder
    }

    /**
     * Mapeia o DTO de Requisição para a Entidade Product.
     * * @param dto     AddProductRequestDTO.
     * 
     * @param company Empresa associada.
     * @return Entidade Product.
     */
    public Product toProductEntity(AddProductRequestDTO dto, Company company) {
        // ... (seu código existente para toProductEntity)
        // [CÓDIGO OMITIDO POR SER INALTERADO]
        Product product = new Product();
        product.setCompany(company);
        product.setNome(dto.getName());
        product.setDescricao(dto.getShortDescription());
        // ... set outros campos
        return product; // Placeholder
    }

    // ... (Outros métodos do mapper, se houver)
}