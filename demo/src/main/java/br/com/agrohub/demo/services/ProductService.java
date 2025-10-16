package br.com.agrohub.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.mappers.ProductMapper;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela lógica de negócios da Entidade Produto.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Retorna todos os produtos ATIVOS no catálogo, mapeados para o formato de Card.
     * Esta é a lógica que alimenta o ClientDashboardScreen.jsx.
     * * @return List<ProductCardResponseDTO>
     */
    public List<ProductCardResponseDTO> findAllActiveProductsCard() {
        
        // 1. Busca todos os produtos ativos usando o método definido no Repository
        // O campo 'active' no Model Product é usado aqui para filtrar.
        List<Product> activeProducts = productRepository.findByActiveTrue(); 
        
        if (activeProducts.isEmpty()) {
            return List.of(); // Retorna uma lista vazia se não houver produtos ativos
        }

        // 2. Mapeia a lista de entidades para a lista de DTOs usando o Mapper
        return activeProducts.stream()
                .map(productMapper::toProductCardDTO) // Usa o método toProductCardDTO
                .collect(Collectors.toList());
    }
    
    /**
     * Retorna um único produto ativo pelo ID no formato de Card.
     * Útil para o ClientDashboardScreen ao clicar no Card.
     * * @param id ID do produto.
     * @return ProductCardResponseDTO
     */
    public ProductCardResponseDTO findProductCardById(Long id) {
        Product product = productRepository.findById(id)
                             // Lançar exceção se não encontrar ou se não estiver ativo
                             .filter(Product::isActive) 
                             .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado ou inativo."));
                             
        // Mapeia o produto encontrado para o DTO de Card
        return productMapper.toProductCardDTO(product);
    }
}