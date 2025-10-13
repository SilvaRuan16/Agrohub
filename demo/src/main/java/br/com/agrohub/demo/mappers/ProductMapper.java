package br.com.agrohub.demo.mappers;

import br.com.agrohub.demo.dto.product.CompanyResumeDTO; // Adicionado e corrigido
import br.com.agrohub.demo.dto.product.ProductCardResponseDTO; // Adicionado e corrigido
import br.com.agrohub.demo.dto.product.ProductDetailResponseDTO; // CORRIGIDO: Agora aponta para dto.product
import br.com.agrohub.demo.dto.product.ComentarioDTO; // CORRIGIDO: Agora aponta para dto.product
import br.com.agrohub.demo.dto.product.AddProductRequestDTO; // Adicionado (DTO de requisição)
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Comment;
import br.com.agrohub.demo.models.AdditionalInfo;
import br.com.agrohub.demo.models.ProductType;
import br.com.agrohub.demo.models.Discount;
import br.com.agrohub.demo.models.Image;
import br.com.agrohub.demo.models.Address; // Necessário para a referência no toProductDetailDTO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre a Entidade Product e seus DTOs de
 * Card, Detalhe e Requisição.
 */
@Component
public class ProductMapper {

    // Injetando CommonMapper
    private final CommonMapper commonMapper;

    @Autowired
    public ProductMapper(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    // =================================================================
    // 1. MAPEAR PARA CARD
    // =================================================================

    // [CÓDIGO DE toProductCardDTO, toCompanyResumeDTO, etc. continua o mesmo]
    public ProductCardResponseDTO toProductCardDTO(Product product) {
        if (product == null)
            return null;

        Double ratingMedio = this.calculateAverageRating(product.getComments());
        Integer totalAvaliacoes = product.getComments() != null ? product.getComments().size() : 0;

        // O construtor abaixo exige que ProductCardResponseDTO tenha um construtor
        // completo ou use @Builder
        // Se der erro, remova o 'new' e use ProductCardResponseDTO.builder()...build()
        return new ProductCardResponseDTO(
                product.getId(),
                product.getName(),
                product.getSalePrice(),
                product.getUnitOfMeasurement(),
                product.getImages().stream().map(Image::getUrl).collect(Collectors.toList()),
                ratingMedio,
                totalAvaliacoes,
                this.toCompanyResumeDTO(product.getCompany()));
    }

    public CompanyResumeDTO toCompanyResumeDTO(Company company) {
        if (company == null)
            return null;

        // O construtor abaixo exige que CompanyResumeDTO tenha um construtor completo
        // ou use @Builder
        return new CompanyResumeDTO(
                company.getId(),
                company.getNomeFantasia() // Corrigindo para o nome da variável comum em Company
        );
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
        dto.setNome(product.getName());
        dto.setPrecoVenda(product.getSalePrice());
        dto.setUnidadeMedida(product.getUnitOfMeasurement());
        dto.setDescricaoCurta(product.getShortDescription());
        dto.setDescricaoDetalhada(product.getDetailedDescription());
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
            dto.setMunicipio(info.getMunicipio());
            dto.setCnpjProdutor(info.getCnpjProdutor());
            // CORRIGIDO: Usando o CommonMapper injetado
            dto.setEnderecoProdutor(info.getAddress() != null ? commonMapper.toAddressDTO(info.getAddress()) : null);
            dto.setTipoProduto(info.getProductType() != null ? info.getProductType().getName() : "Não especificado");
        }

        // DESCONTOS
        Discount activeDiscount = product.getDiscounts().stream()
                .filter(Discount::isActive)
                .findFirst().orElse(null);

        if (activeDiscount != null) {
            dto.setDescontoMaximo(activeDiscount.getMaxPercentage());
        }

        return dto;
    }

    // [CÓDIGO dos métodos auxiliares toComentarioDTOList e calculateAverageRating
    // continua o mesmo]
    private List<ComentarioDTO> toComentarioDTOList(List<Comment> comments) {
        if (comments == null)
            return List.of();

        // O construtor abaixo exige que ComentarioDTO tenha um construtor completo ou
        // use @Builder
        return comments.stream().map(comment -> new ComentarioDTO(
                comment.getId(),
                comment.getClient().getFullName(),
                comment.getCommentText(),
                comment.getRating(),
                comment.getCommentDate())).collect(Collectors.toList());
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

    // [CÓDIGO de toProductEntity e toAdditionalInfoEntity continua o mesmo]
    public Product toProductEntity(AddProductRequestDTO dto, Company company) {
        Product product = new Product();

        product.setCompany(company);
        product.setName(dto.getName());
        product.setShortDescription(dto.getShortDescription());
        product.setDetailedDescription(dto.getDetailedDescription());
        product.setSalePrice(dto.getSalePrice());
        product.setCostPrice(dto.getCostPrice());
        product.setMinStock(dto.getMinStock());
        product.setCurrentStock(dto.getInitialStock());
        product.setUnitOfMeasurement(dto.getUnitOfMeasurement());

        return product;
    }

    public AdditionalInfo toAdditionalInfoEntity(AddProductRequestDTO dto, Product product, ProductType productType) {
        AdditionalInfo info = new AdditionalInfo();
        info.setProdutor(dto.getProdutorName());
        info.setMunicipio(dto.getMunicipality());
        info.setCnpjProdutor(dto.getProducerCnpj());
        info.setProductType(productType);

        // Mapeamento de endereço deve ser feito no Service ou usando o CommonMapper
        // info.setAddress(commonMapper.toAddressEntity(dto.getProducerAddress()));

        return info;
    }
}