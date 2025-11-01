package br.com.agrohub.demo.services;

import java.nio.file.AccessDeniedException; // Necessário para a exceção
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Necessário para o método
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO; // Necessário para o DTO de retorno
import br.com.agrohub.demo.mappers.ProductMapper;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType; // Necessário para verificação do tipo de usuário
import br.com.agrohub.demo.repository.CompanyRepository; // Novo Repositório
import br.com.agrohub.demo.repository.ProductRepository;
import br.com.agrohub.demo.repository.UserRepository; // Novo Repositório
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela lógica de negócios da Entidade Produto.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
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
            return List.of();
        }

        // 2. Mapeia a lista de entidades para a lista de DTOs usando o Mapper
        return activeProducts.stream()
                .map(productMapper::toProductCardDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retorna um único produto ativo pelo ID no formato de Card.
     * 
     * @param id ID do produto.
     * @return ProductCardResponseDTO
     */
    public ProductCardResponseDTO findProductCardById(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado ou inativo."));

        // Mapeia o produto encontrado para o DTO de Card
        return productMapper.toProductCardDTO(product);
    }

    // =========================================================================
    // 🎯 NOVO MÉTODO (Lógica de Produção para o Dashboard da Empresa)
    // =========================================================================

    /**
     * Lógica de Produção: Retorna produtos da empresa autenticada.
     * Requer que o usuário logado seja do tipo EMPRESA.
     * * @param authentication Objeto de autenticação do Spring Security.
     * 
     * @return Lista de ProductDetailResponseDTO.
     */
    public List<ProductDetailResponseDTO> findProductsForLoggedInCompany(Authentication authentication)
            throws AccessDeniedException {

        // 1. Extrai o "username" (geralmente o email) do token JWT
        String userEmail = authentication.getName();

        // 2. Localiza o Usuário
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado ou inativo."));

        // 3. Verifica o tipo de usuário (segurança por papel)
        if (user.getTipoUsuario() != UserType.EMPRESA) {
            // Lança uma exceção para ser capturada e retornar 403 (FORBIDDEN) no Controller
            throw new AccessDeniedException("Acesso negado. Apenas empresas podem acessar o dashboard.");
        }

        // 4. Localiza a Empresa associada a este Usuário
        // Assume-se que CompanyRepository possui o método findByUserId
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o usuário logado."));

        // 5. Busca os produtos usando o ID da empresa (método que você já tem no
        // ProductRepository)
        List<Product> companyProducts = productRepository.findByCompanyId(company.getId());

        if (companyProducts.isEmpty()) {
            return List.of();
        }

        // 6. Mapeia para o DTO de Detalhe
        return companyProducts.stream()
                .map(productMapper::toProductDetailDTO)
                .collect(Collectors.toList());
    }
}