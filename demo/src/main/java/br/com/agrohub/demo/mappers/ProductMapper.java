package br.com.agrohub.demo.mappers;

// DTOs (Assumindo que estão no pacote base 'dto', conforme a estrutura de arquivos)
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.AddProductRequestDTO;
import br.com.agrohub.demo.dto.ComentarioDTO; // Necessário para o CommonMapper
import br.com.agrohub.demo.dto.CompanyResumeDTO;
import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
import br.com.agrohub.demo.models.AdditionalInfo;
import br.com.agrohub.demo.models.Comment;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Discount;
import br.com.agrohub.demo.models.Image;
import br.com.agrohub.demo.models.Product; // Necessário para o ComentarioDTO
import br.com.agrohub.demo.models.ProductType;

/**
 * Mapper responsável pela conversão entre a Entidade Product e seus DTOs de
 * Card, Detalhe e Requisição.
 */
@Component
public class ProductMapper {

    private final CommonMapper commonMapper;

    public ProductMapper(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    // =================================================================
    // 1. MAPEAR PARA CARD
    // =================================================================

    public ProductCardResponseDTO toProductCardDTO(Product product) {
        if (product == null)
            return null;

        Double ratingMedio = this.calculateAverageRating(product.getComments());
        Integer totalAvaliacoes = product.getComments() != null ? product.getComments().size() : 0;

        return new ProductCardResponseDTO(
                product.getId(), // -> Mapeia para o campo 'id'
                product.getNome(), // -> Mapeia para o campo 'nome'
                product.getPrecoVenda(), // -> Mapeia para o campo 'precoVenda'
                product.getUnitOfMeasurement(), // -> Mapeia para o campo 'unidadeMedida'
                product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()), // -> Mapeia para o campo
                                                                                              // 'imagensUrls'
                ratingMedio, // -> Mapeia para o campo 'ratingMedio'
                totalAvaliacoes, // -> Mapeia para o campo 'totalAvaliacoes'
                this.toCompanyResumeDTO(product.getCompany())); // -> Mapeia para o campo 'empresa'
    }

    public CompanyResumeDTO toCompanyResumeDTO(Company company) {
        if (company == null)
            return null;

        return new CompanyResumeDTO(
                company.getId(),
                company.getNomeFantasia());
    }

    // =================================================================
    // 2. MAPEAR PARA DETALHE
    // =================================================================

    public ProductDetailResponseDTO toProductDetailDTO(Product product) {
        if (product == null)
            return null;

        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();

        // DADOS PRINCIPAIS (Product)
        dto.setId(product.getId());
        dto.setNome(product.getNome());
        dto.setPrecoVenda(product.getPrecoVenda());
        dto.setUnidadeMedida(product.getUnitOfMeasurement());
        dto.setDescricaoCurta(product.getDescricao());
        dto.setDescricaoDetalhada(product.getDescricao());
        dto.setImagensUrls(product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));

        // AVALIAÇÃO (Comments)
        dto.setRatingMedio(this.calculateAverageRating(product.getComments()));
        dto.setTotalAvaliacoes(product.getComments() != null ? product.getComments().size() : 0);
        dto.setComentarios(this.toComentarioDTOList(product.getComments()));

        // EMPRESA (Company)
        dto.setEmpresa(this.toCompanyResumeDTO(product.getCompany()));

        // INFORMAÇÕES ADICIONAIS (AdditionalInfo)
        AdditionalInfo info = product.getAdditionalInfo();
        if (info != null) {
            dto.setNomeProdutor(info.getProdutor());
            dto.setMunicipioEmpresa(info.getMunicipio());
            dto.setCnpjProdutor(info.getCnpjProdutor());
            dto.setEnderecoProdutor(info.getAddress() != null ? commonMapper.toAddressDTO(info.getAddress()) : null);
            dto.setTipoProduto(info.getProductType() != null ? info.getProductType().getTipo() : "Não especificado");
        }

        // DESCONTOS
        // 🎯 CORREÇÃO APLICADA AQUI: Usando product.getDiscount() para @OneToOne e
        // checando se está ativo
        // DESCONTOS
        Discount activeDiscount = product.getDiscount();

        // 🎯 CORREÇÃO 1: Mantendo isAtivo(), mas você deve checar se esse é o nome do
        // método em Discount
        if (activeDiscount != null && activeDiscount.isAtivo()) {

            // 🎯 CORREÇÃO 2: Conversão de BigDecimal para Double
            dto.setDescontoMaximo(activeDiscount.getPercentual().doubleValue());

        } else {
            dto.setDescontoMaximo(null);
        }

        return dto;
    }

    private List<ComentarioDTO> toComentarioDTOList(List<Comment> comments) {
        if (comments == null)
            return List.of();

        return comments.stream().map(comment -> new ComentarioDTO(
                comment.getId(),
                // Getters em Português de Client e Comment
                comment.getClient().getNomeCompleto(),
                comment.getComentario(),
                comment.getRating(),
                comment.getDataComentario())).collect(Collectors.toList());
    }

    private Double calculateAverageRating(List<Comment> comments) {
        if (comments == null || comments.isEmpty())
            return 0.0;

        return comments.stream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0.0);
    }

    // =================================================================
    // 3. MAPEAR PARA ENTIDADES
    // =================================================================

    public Product toProductEntity(AddProductRequestDTO dto, Company company) {
        Product product = new Product();

        product.setCompany(company);
        // Setters em Português do Model Product
        product.setNome(dto.getName());
        product.setDescricao(dto.getShortDescription());
        product.setDescricao(dto.getDetailedDescription());
        product.setPrecoVenda(dto.getSalePrice());
        product.setPrecoCompra(dto.getCostPrice());
        product.setQuantidadeMinEstoque(dto.getMinStock()); // CORRIGIDO: Assume-se que o setter é
                                                            // setQuantidadeMinEstoque
        product.setQuantidadeEstoque(dto.getInitialStock()); // CORRIGIDO: Assume-se que o setter é setQuantidadeEstoque
        product.setUnitOfMeasurement(dto.getUnitOfMeasurement()); // <-- CORRIGIDO AQUI

        return product;
    }

    public AdditionalInfo toAdditionalInfoEntity(AddProductRequestDTO dto, Product product, ProductType productType) {
        AdditionalInfo info = new AdditionalInfo();
        // Setters em Português do Model AdditionalInfo
        info.setProdutor(dto.getProdutorName());
        info.setMunicipio(dto.getMunicipality());
        info.setCnpjProdutor(dto.getProducerCnpj());
        info.setProductType(productType);

        // Mapeamento de endereço deve ser feito no Service ou usando o CommonMapper
        // info.setAddress(commonMapper.toAddressEntity(dto.getProducerAddress()));

        return info;
    }
}