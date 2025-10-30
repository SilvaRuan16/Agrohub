package br.com.agrohub.demo.services;

import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
import br.com.agrohub.demo.mappers.ProductMapper;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.repository.ProductRepository;
import br.com.agrohub.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócios da Entidade Produto.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository; // INJETADO

    public ProductService(ProductRepository productRepository, ProductMapper productMapper,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository; // ADICIONADO AO CONSTRUTOR
    }

    /**
     * Retorna todos os produtos ATIVOS no catálogo, mapeados para o formato de
     * Card.
     * Esta é a lógica que alimenta o ClientDashboardScreen.jsx.
     * 
     * @return List<ProductCardResponseDTO>
     */
    public List<ProductCardResponseDTO> findAllActiveProductsCard() {

        // 1. Busca todos os produtos ativos usando o método definido no Repository
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
     * 
     * @param id ID do produto.
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

    /**
     * Busca os produtos para a empresa autenticada.
     */
    public List<ProductDetailResponseDTO> findProductsForLoggedInCompany(Authentication authentication)
            throws AccessDeniedException {
        // 1. Pega o identificador (email/cpf/cnpj) do usuário logado do token JWT
        String userIdentifier = authentication.getName();

        // 2. Busca o User no banco de dados. O nome no token (getName()) é o
        // identificador único (email/cpf/cnpj)
        User user = userRepository.findByEmailOrCpfOrCnpj(userIdentifier, userIdentifier, userIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + userIdentifier));

        // 3. Pega a Empresa associada a este User (relação One-to-One no User.java ou
        // Company.java)
        Company company = user.getCompany(); // Baseado no model User.java
        if (company == null) {
            // Se o usuário não tem uma empresa (ex: é um CLIENTE), ele não pode acessar
            // esta rota.
            throw new AccessDeniedException("Este usuário não está associado a uma empresa.");
        }

        // 4. Busca os produtos usando o ID da empresa e o método que já existe no
        // repository
        List<Product> companyProducts = productRepository.findByCompanyId(company.getId());

        // 5. Mapeia a lista de Entidades para a lista de DTOs de Detalhe
        return companyProducts.stream()
                .map(productMapper::toProductDetailDTO) // Usando o mapper que já existe
                .collect(Collectors.toList());
    }
}