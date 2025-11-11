package br.com.agrohub.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.agrohub.demo.dto.AddProductRequestDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    // --- Mocks das Dependências ---
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private AuthSecurity authSecurity;
    @Mock
    private Authentication authentication;

    // --- Serviço a ser testado ---
    @InjectMocks
    private ProductService productService;

    // --- Objetos de Teste Reutilizáveis ---
    private Product mockProduct;
    private ProductDetailResponseDTO mockDetailDTO;
    private User mockUser;
    private Company mockCompany;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setNome("Café Arábica Orgânico");

        mockDetailDTO = new ProductDetailResponseDTO();
        mockDetailDTO.setId(1L);
        // 🟢 CORREÇÃO: Usando setNome para corresponder ao campo 'nome' esperado
        mockDetailDTO.setNome("Café Arábica Orgânico"); 

        mockUser = new User();
        mockUser.setId(99L);
        mockUser.setTipoUsuario(UserType.EMPRESA);

        mockCompany = new Company();
        mockCompany.setId(5L);
        mockCompany.setUser(mockUser);
    }

    // =========================================================================
    // TESTES DO MÉTODO addProduct (Cadastro de Produtos)
    // =========================================================================

    @Test
    @DisplayName("Deve adicionar um produto com sucesso quando o usuário e a empresa existirem")
    void addProduct_Success() {
        // Cenário
        AddProductRequestDTO requestDTO = new AddProductRequestDTO();
        Product productToSave = new Product();

        // Configuração dos Mocks
        when(authSecurity.getLoggedInUserId(authentication)).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.of(mockUser));
        when(companyRepository.findByUserId(99L)).thenReturn(Optional.of(mockCompany));
        when(productMapper.toProductEntity(requestDTO, mockCompany)).thenReturn(productToSave);

        // Ação
        productService.addProduct(requestDTO, authentication);

        // Verificação
        verify(productRepository).save(productToSave); // Verifica se o save foi chamado
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException se o usuário logado não for encontrado")
    void addProduct_UserNotFound() {
        // Cenário
        AddProductRequestDTO requestDTO = new AddProductRequestDTO();

        // Configuração dos Mocks
        when(authSecurity.getLoggedInUserId(authentication)).thenReturn(99L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty()); // Usuário não encontrado

        // Ação e Verificação
        assertThrows(UsernameNotFoundException.class, () -> {
            productService.addProduct(requestDTO, authentication);
        });

        // Verifica se a persistência NÃO foi chamada
        verify(productRepository, never()).save(any(Product.class)); 
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException se a empresa do usuário não for encontrada")
    void addProduct_CompanyNotFound() {
        // Cenário
        AddProductRequestDTO requestDTO = new AddProductRequestDTO();

        // Configuração dos Mocks
        when(authSecurity.getLoggedInUserId(authentication)).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.of(mockUser));
        when(companyRepository.findByUserId(anyLong())).thenReturn(Optional.empty()); // Empresa não encontrada

        // Ação e Verificação
        assertThrows(EntityNotFoundException.class, () -> {
            productService.addProduct(requestDTO, authentication);
        });

        // Verifica se a persistência NÃO foi chamada
        verify(productRepository, never()).save(any(Product.class));
    }


    // =========================================================================
    // TESTES DO MÉTODO findProductDetailById
    // =========================================================================
    
    @Test
    @DisplayName("Deve retornar ProductDetailDTO quando o ID for encontrado")
    void findProductDetailById_Found() {
        // Configuração dos Mocks
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productMapper.toProductDetailDTO(mockProduct)).thenReturn(mockDetailDTO);

        // Ação
        ProductDetailResponseDTO result = productService.findProductDetailById(1L); 

        // Verificação
        assertEquals(1L, result.getId());
        // 🟢 CORREÇÃO: Usando getNome para corresponder ao campo 'nome' esperado
        assertEquals("Café Arábica Orgânico", result.getNome()); 
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o ID NÃO for encontrado")
    void findProductDetailById_NotFound() {
        // Configuração dos Mocks
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ação e Verificação
        assertThrows(EntityNotFoundException.class, () -> {
            productService.findProductDetailById(2L); 
        });

        // Verifica se o mapper NUNCA foi chamado
        verify(productMapper, never()).toProductDetailDTO(any(Product.class));
    }
}