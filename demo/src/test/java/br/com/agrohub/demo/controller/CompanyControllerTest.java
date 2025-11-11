package br.com.agrohub.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ContactDTO;
import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.dto.HistoricoVendaDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
import br.com.agrohub.demo.exceptions.ResourceNotFoundException;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider;
import br.com.agrohub.demo.services.CompanyService;
import br.com.agrohub.demo.services.ProductService;

@WebMvcTest(controllers = CompanyController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ActiveProfiles("test")
public class CompanyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private CompanyService companyService;

        @MockBean
        private ProductService productService;

        @MockBean
        private JwtTokenProvider jwtTokenProvider;

        @MockBean
        private UserDetailsService userDetailsService;

        private CompanyRegisterRequestDTO registerRequest;
        private CompanyProfileResponseDTO profileResponse;
        private ProductDetailResponseDTO productDetail;
        private final Long existingCompanyId = 2L;
        private final Long nonExistingCompanyId = 99L;

        @BeforeEach
        void setUp() {
                // 1. Configuração dos DTOs Aninhados Reais
                ContactDTO contact = new ContactDTO();
                contact.setEmail("agrohub@empresa.com.br");
                contact.setTelefone("998887777");
                contact.setWebsite("www.agrohub.com");

                EnderecoDTO endereco = new EnderecoDTO(
                                1L, "Rua do Agro", "100", "Bairro Produtivo", "Fazenda X", "ESTADO", "12345-000",
                                "Galpão Principal",
                                "Rua do Agro");

                // 2. Configuração do DTO de Registro
                registerRequest = new CompanyRegisterRequestDTO(
                                contact.getEmail(), // 1. email (String)
                                "senhaEmpresa123", // 2. senha (String)
                                "00.123.456/0001-00", // 3. cnpj (String)
                                "Agrohub Soluções Ltda", // 4. razaoSocial (String)
                                "Agrohub", // 5. nomeFantasia (String)
                                LocalDate.of(2010, 5, 10), // 6. dataFundacao (LocalDate)
                                contact.getTelefone(), // 7. telefone (String)
                                contact.getWebsite(), // 8. urlSite (String)
                                contact, // 9. contact (ContactDTO)
                                endereco // 10. endereco (EnderecoDTO)
                );

                // Simulação de Histórico de Vendas (Usando o DTO REAL)
                HistoricoVendaDTO historicoVenda = new HistoricoVendaDTO(
                                1001L,
                                "Semente Híbrida XPTO", // nomeProduto
                                50, // quantidade
                                new BigDecimal("15000.00"), // valorTotal
                                LocalDateTime.now().minusDays(5), // dataVenda
                                "CONCLUIDO" // statusVenda
                );

                // 3. Configuração do DTO de Perfil
                profileResponse = new CompanyProfileResponseDTO();
                profileResponse.setId(existingCompanyId);
                profileResponse.setEmail(contact.getEmail());
                profileResponse.setRazaoSocial("Agrohub Soluções Ltda");
                profileResponse.setNomeFantasia("Agrohub");
                profileResponse.setCnpj("00.123.456/0001-00");
                profileResponse.setDataFundacao(LocalDate.of(2010, 5, 10));
                profileResponse.setTelefone(contact.getTelefone());
                profileResponse.setUrlSite(contact.getWebsite());
                profileResponse.setLogoUrl("http://logo.com/agrohub.png");
                profileResponse.setEnderecos(Collections.singletonList(endereco));
                profileResponse.setHistoricoVendas(Collections.singletonList(historicoVenda));

                // 4. Configuração do DTO de Detalhe de Produto
                productDetail = new ProductDetailResponseDTO();
                productDetail.setId(10L);
                productDetail.setNome("Fertilizante Orgânico Padrão");
                productDetail.setCodigoInterno("PROD001");
        }

        @Test
        @DisplayName("testRegisterCompany_WhenRequestIsValid_ShouldReturn201Created")
        void testRegisterCompany_WhenRequestIsValid_ShouldReturn201Created() throws Exception {
                // Arrange
                doNothing().when(companyService).registerCompany(any(CompanyRegisterRequestDTO.class));

                // Act & Assert
                mockMvc.perform(post("/api/v1/companies/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))

                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @DisplayName("testGetCompanyProfile_WhenCompanyExists_ShouldReturn200OkAndProfile")
        void testGetCompanyProfile_WhenCompanyExists_ShouldReturn200OkAndProfile() throws Exception {
                // Arrange
                when(companyService.getCompanyProfile(existingCompanyId)).thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(get("/api/v1/companies/{companyId}/profile", existingCompanyId)
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(existingCompanyId.intValue())))
                                .andExpect(jsonPath("$.nomeFantasia", is("Agrohub")))
                                // Verifica um campo aninhado no HistoricoVendas
                                .andExpect(jsonPath("$.historicoVendas[0].nomeProduto", is("Semente Híbrida XPTO")));
        }

        @Test
        @DisplayName("testGetCompanyProfile_WhenCompanyDoesNotExist_ShouldReturn404NotFound")
        void testGetCompanyProfile_WhenCompanyDoesNotExist_ShouldReturn404NotFound() throws Exception {
                // Arrange
                doThrow(new ResourceNotFoundException("Empresa", nonExistingCompanyId))
                                .when(companyService).getCompanyProfile(nonExistingCompanyId);

                // Act & Assert
                mockMvc.perform(get("/api/v1/companies/{companyId}/profile", nonExistingCompanyId)
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @WithMockUser(username = "agrohub@empresa.com.br", roles = { "COMPANY" })
        @DisplayName("testGetMyProducts_WhenProductsExist_ShouldReturn200OkAndList")
        void testGetMyProducts_WhenProductsExist_ShouldReturn200OkAndList() throws Exception {
                // Arrange
                List<ProductDetailResponseDTO> products = Collections.singletonList(productDetail);

                // CORREÇÃO FINAL: Usando any() para garantir que o mock seja ativado
                // independentemente da implementação do Authentication.
                when(productService.findProductsForLoggedInCompany(any())).thenReturn(products);

                // Act & Assert
                mockMvc.perform(get("/api/v1/companies/dashboard")
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].codigoInterno", is("PROD001")));
        }

        @Test
        @DisplayName("testGetMyProducts_WhenForbiddenAccess_ShouldReturn403Forbidden")
        void testGetMyProducts_WhenForbiddenAccess_ShouldReturn403Forbidden() throws Exception {
                // Arrange
                doThrow(new RuntimeException("Acesso negado")).when(productService)
                                .findProductsForLoggedInCompany(any());

                // Act & Assert
                mockMvc.perform(get("/api/v1/companies/dashboard")
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isForbidden());
        }
}