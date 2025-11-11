package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.CompanyResumeDTO; // Import real
import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.exceptions.ResourceNotFoundException;
import br.com.agrohub.demo.services.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService; // Mock do serviço

    // DTOs de exemplo para os testes
    private ProductCardResponseDTO productCard;
    private CompanyResumeDTO companyResume; // DTO real
    private final Long existingProductId = 10L;
    private final Long nonExistingProductId = 99L;

    @BeforeEach
    void setUp() {
        // 1. Configuração do DTO aninhado
        // CompanyResumeDTO(Long id, String nomeFantasia)
        companyResume = new CompanyResumeDTO(5L, "AgroVendas SA");

        // 2. Configuração do DTO de Resposta do Card
        productCard = new ProductCardResponseDTO(
                existingProductId, // id
                "Adubo Orgânico Max", // nome
                new BigDecimal("125.50"), // precoVenda
                "KG", // unidadeMedida
                List.of("http://img.com/adubo.jpg"), // imagensUrls
                4.8, // ratingMedio
                150, // totalAvaliacoes
                companyResume // empresa (usando o DTO real)
        );
    }

    // --- TESTE 1: GET /api/v1/products (Lista de Produtos) ---

    @Test
    @DisplayName("testGetAllActiveProducts_WhenProductsExist_ShouldReturn200OkAndList")
    void testGetAllActiveProducts_WhenProductsExist_ShouldReturn200OkAndList() throws Exception {
        // Arrange: Simula o serviço retornando uma lista com 1 produto
        List<ProductCardResponseDTO> products = Collections.singletonList(productCard);
        when(productService.findAllActiveProductsCard()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products") // Rota GET base
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()) // Espera Status 200 OK
                .andExpect(jsonPath("$", hasSize(1))) // Verifica se a lista tem 1 item
                .andExpect(jsonPath("$[0].nome", is("Adubo Orgânico Max"))) // Verifica o nome
                .andExpect(jsonPath("$[0].empresa.nomeFantasia", is("AgroVendas SA"))); // Verifica o DTO aninhado
    }

    // --- TESTE 2: GET /api/v1/products/{id} (Busca por ID) ---

    @Test
    @DisplayName("testGetProductById_WhenProductExists_ShouldReturn200OkAndProductCard")
    void testGetProductById_WhenProductExists_ShouldReturn200OkAndProductCard() throws Exception {
        // Arrange: Simula o serviço retornando o DTO do card
        when(productService.findProductCardById(existingProductId)).thenReturn(productCard);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/{id}", existingProductId) // Rota GET com ID
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()) // Espera Status 200 OK
                .andExpect(jsonPath("$.id", is(existingProductId.intValue()))) // Verifica o ID
                .andExpect(jsonPath("$.precoVenda", is(125.50))); // Verifica o preço (como double/float para o JSON
                                                                  // Path)
    }

    @Test
    @DisplayName("testGetProductById_WhenProductDoesNotExist_ShouldReturn404NotFound")
    void testGetProductById_WhenProductDoesNotExist_ShouldReturn404NotFound() throws Exception {
        // Arrange: Simula o serviço lançando a ResourceNotFoundException
        when(productService.findProductCardById(nonExistingProductId))
                .thenThrow(new ResourceNotFoundException("Produto", nonExistingProductId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/{id}", nonExistingProductId)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound()); // Espera Status 404 Not Found devido à ResourceNotFoundException
    }
}