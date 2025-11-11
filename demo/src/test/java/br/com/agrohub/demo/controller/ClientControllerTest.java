package br.com.agrohub.demo.controller;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.dto.ContactDTO;
import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.exceptions.ResourceNotFoundException;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider;
import br.com.agrohub.demo.services.ClientService;

@WebMvcTest(controllers = ClientController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ActiveProfiles("test")
public class ClientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private ClientService clientService;

        @MockBean
        private JwtTokenProvider jwtTokenProvider;

        @MockBean
        private UserDetailsService userDetailsService;

        private ClientRegisterRequestDTO registerRequest;
        private ClientProfileResponseDTO profileResponse;
        private final Long existingClientId = 1L;
        private final Long nonExistingClientId = 99L;

        @BeforeEach
        void setUp() {

                // Usando o ContactDTO REAL
                ContactDTO contact = new ContactDTO();
                contact.setEmail("client@teste.com");
                contact.setTelefone("998887777");
                contact.setRedeSocial("linkedin.com/joao");
                contact.setWebsite("joaodasilva.com");

                // Usando o EnderecoDTO REAL
                // Construtor: (Long id, String rua, String numero, String bairro, String
                // cidade, String estado, String cep, String complemento, String logradouro)
                EnderecoDTO endereco = new EnderecoDTO(
                                1L, "Rua A", "10", "Centro", "Cidade X", "ESTADO", "12345-678", "Apto 101", "Rua A");

                // --- 2. Configuração do DTO de Registro
                // Construtor: (String senha, String cpf, String nomeCompleto, String rg, String
                // cnpj, LocalDate dataNascimento, ContactDTO contact, EnderecoDTO endereco)
                registerRequest = new ClientRegisterRequestDTO(
                                "senha123", // 1. senha (String)
                                "123.456.789-00", // 2. cpf (String)
                                "João da Silva", // 3. nomeCompleto (String)
                                "12.345.678-9", // 4. rg (String)
                                "", // 5. cnpj (String) - CORREÇÃO
                                LocalDate.of(1990, 1, 1), // 6. dataNascimento (LocalDate)
                                contact, // 7. contact (ContactDTO)
                                endereco // 8. endereco (EnderecoDTO)
                );

                // --- 3. Configuração do DTO de Perfil
                profileResponse = new ClientProfileResponseDTO();
                profileResponse.setId(existingClientId);
                profileResponse.setEmail(contact.getEmail());
                profileResponse.setNomeCompleto("João da Silva");
                profileResponse.setCpf("123.456.789-00");
                profileResponse.setCnpj("");
                profileResponse.setTelefone(contact.getTelefone());
                profileResponse.setRedeSocial(contact.getRedeSocial()); // Campo novo do ContactDTO
                profileResponse.setWebsite(contact.getWebsite()); // Campo novo do ContactDTO
                profileResponse.setEnderecos(Collections.singletonList(endereco));

                // Simulação de Histórico
                HistoricoPedidoDTO historico = new HistoricoPedidoDTO(
                                1001L, "Semente Premium", 2, new BigDecimal("500.00"), LocalDateTime.now(), "ENTREGUE");
                profileResponse.setHistoricoPedidos(Collections.singletonList(historico));
        }

        @Test
        @DisplayName("testRegisterClient_WhenRequestIsValid_ShouldReturn201Created")
        void testRegisterClient_WhenRequestIsValid_ShouldReturn201Created() throws Exception {
                // Arrange
                doNothing().when(clientService).registerClient(any(ClientRegisterRequestDTO.class));

                // Act & Assert
                mockMvc.perform(post("/api/v1/clients/register") // Rota POST
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))

                                .andExpect(status().isCreated()) // Espera Status 201 Created
                                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @DisplayName("testGetClientProfile_WhenClientExists_ShouldReturn200OkAndProfile")
        void testGetClientProfile_WhenClientExists_ShouldReturn200OkAndProfile() throws Exception {
                // Arrange
                when(clientService.getClientProfile(existingClientId)).thenReturn(profileResponse);

                // Act & Assert
                mockMvc.perform(get("/api/v1/clients/{clientId}/profile", existingClientId) // Rota GET
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isOk()) // Espera Status 200 OK
                                .andExpect(jsonPath("$.id", is(existingClientId.intValue())))
                                .andExpect(jsonPath("$.nomeCompleto", is("João da Silva"))) // Verifica o nome
                                .andExpect(jsonPath("$.website", is("joaodasilva.com"))) // Verifica o campo do
                                                                                         // ContactDTO
                                .andExpect(jsonPath("$.enderecos[0].cep", is("12345-678"))); // Verifica o DTO aninhado
        }

        @Test
        @DisplayName("testGetClientProfile_WhenClientDoesNotExist_ShouldReturn404NotFound")
        void testGetClientProfile_WhenClientDoesNotExist_ShouldReturn404NotFound() throws Exception {
                // Arrange
                doThrow(new ResourceNotFoundException("Cliente", nonExistingClientId))
                                .when(clientService).getClientProfile(nonExistingClientId);

                // Act & Assert
                mockMvc.perform(get("/api/v1/clients/{clientId}/profile", nonExistingClientId)
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isNotFound()) // Espera Status 404 Not Found
                                .andExpect(jsonPath("$").doesNotExist());
        }
}