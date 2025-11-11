package br.com.agrohub.demo.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.AddProductRequestDTO;
import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO; // Importe o DTO de Detalhe
import br.com.agrohub.demo.mappers.ProductMapper;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.CompanyRepository;
import br.com.agrohub.demo.repository.ProductRepository;
import br.com.agrohub.demo.repository.UserRepository;
import br.com.agrohub.demo.security.AuthSecurity;
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
        private final AuthSecurity authSecurity;

        // Construtor
        public ProductService(
                        ProductRepository productRepository,
                        ProductMapper productMapper,
                        UserRepository userRepository,
                        CompanyRepository companyRepository,
                        AuthSecurity authSecurity) {
                this.productRepository = productRepository;
                this.productMapper = productMapper;
                this.userRepository = userRepository;
                this.companyRepository = companyRepository;
                this.authSecurity = authSecurity;
        }

        // --- CADASTRO ---

        /**
         * Adiciona um novo produto, vinculando-o à empresa do usuário autenticado.
         */
        public void addProduct(AddProductRequestDTO requestDTO, Authentication authentication)
                        throws UsernameNotFoundException, EntityNotFoundException {

                // 1. Localiza o ID do Usuário logado
                Long userId = authSecurity.getLoggedInUserId(authentication);

                // 2. Localiza o Usuário
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuário logado não encontrado."));

                // 3. Localiza a Empresa
                Company company = companyRepository.findByUserId(user.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Empresa não encontrada para o usuário logado."));

                // 4. Mapeia e salva
                Product product = productMapper.toProductEntity(requestDTO, company);
                productRepository.save(product);
        }

        // --- CONSULTA PARA CLIENTE (Card) ---

        public List<ProductCardResponseDTO> findAllActiveProductsCard() {
                // Assume que productRepository.findAll() ou similar é usado aqui
                return productRepository.findAll().stream()
                                .map(productMapper::toProductCardDTO)
                                .collect(Collectors.toList());
        }

        public ProductCardResponseDTO findProductCardById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
                return productMapper.toProductCardDTO(product);
        }

        // --- NOVO MÉTODO DE CONSULTA PARA DETALHE (Usado para o ProductDetailScreen)
        // ---

        /**
         * Busca um único produto ativo pelo ID e retorna o DTO de Detalhe.
         * Este é o método que o seu teste estava a tentar chamar.
         */
        public ProductDetailResponseDTO findProductDetailById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

                // ⚠️ ATENÇÃO: Certifique-se que o ProductMapper tem o método
                // 'toProductDetailDTO(Product product)'
                return productMapper.toProductDetailDTO(product);
        }

        // --- CONSULTA PARA EMPRESA (Dashboard) ---

        /**
         * Busca os produtos da empresa autenticada.
         */
        @SuppressWarnings("null")
        public List<ProductDetailResponseDTO> findProductsForLoggedInCompany(Authentication authentication)
                        throws AccessDeniedException {

                Long userId = authSecurity.getLoggedInUserId(authentication);

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Usuário logado não encontrado ou inativo."));

                if (user.getTipoUsuario() != UserType.EMPRESA) {
                        throw new AccessDeniedException("Acesso negado. Apenas empresas podem acessar o dashboard.");
                }

                Company company = companyRepository.findByUserId(user.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Empresa não encontrada para o usuário logado."));

                List<Product> companyProducts = productRepository.findByCompanyId(company.getId());

                return companyProducts.stream()
                                .map(productMapper::toProductDetailDTO)
                                .collect(Collectors.toList());
        }
}