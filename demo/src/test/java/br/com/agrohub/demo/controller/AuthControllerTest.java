package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // Variáveis de instância para os DTOs de teste
    private LoginRequestDTO loginRequest;
    private LoginResponseDTO loginResponse;
    private PasswordResetRequestDTO resetRequest;

    @BeforeEach
    void setUp() {
        // *** MÉTODO ATUALIZADO ***

        // 1. Configura o DTO de requisição de login
        loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("cliente@email.com");
        loginRequest.setUserType("CLIENTE");
        loginRequest.setPassword("senha123");

        // 2. Configura o DTO de resposta de login
        // Usando o construtor completo: LoginResponseDTO(String token, String message,
        // String userType)
        loginResponse = new LoginResponseDTO("fake-jwt-token", "Login bem-sucedido", "CLIENTE");

        // 3. Configura o DTO de requisição de reset de senha
        resetRequest = new PasswordResetRequestDTO();
        resetRequest.setEmail("cliente@email.com");
        resetRequest.setTokenVerificacao("123456"); //
        resetRequest.setNewPassword("novaSenha456"); //
    }

    @Test
    @DisplayName("testLogin_WhenValidCredentials_ShouldReturn200AndToken")
    void testLogin_WhenValidCredentials_ShouldReturn200AndToken() throws Exception {
        // Arrange
        when(authService.authenticateUser(any(LoginRequestDTO.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))

                // Assertions (Verificações)
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.token", is("fake-jwt-token"))) // Verifica o token
                .andExpect(jsonPath("$.message", is("Login bem-sucedido"))) // Verifica a mensagem
                .andExpect(jsonPath("$.userType", is("CLIENTE"))); // Verifica o userType
    }

    @Test
    @DisplayName("testResetPassword_WhenRequestIsValid_ShouldReturn200OK")
    void testResetPassword_WhenRequestIsValid_ShouldReturn200OK() throws Exception {
        // Arrange
        // O método no service (resetPassword) é 'void', então usamos doNothing()
        doNothing().when(authService).resetPassword(any(PasswordResetRequestDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetRequest))) // Envia o JSON completo

                // Assertion (Verificação)
                .andExpect(status().isOk()); // Espera 200 OK
    }
}