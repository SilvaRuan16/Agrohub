package br.com.agrohub.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.exceptions.InvalidCredentialsException;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.UserRepository;
import br.com.agrohub.demo.security.CustomUserDetails;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider;

/**
 * Testes unitários para o AuthService.
 * Utiliza Mockito para simular as dependências (Repositories, Security
 * Components).
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // Instância real do serviço que queremos testar
    @InjectMocks
    private AuthService authService;

    // Mocks para as dependências
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider; // Injetado automaticamente via @InjectMocks

    // Objetos comuns para simulações
    private LoginRequestDTO loginRequest;
    private User user;
    private Authentication authentication;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configuração padrão do DTO de requisição
        loginRequest = new LoginRequestDTO();
        loginRequest.setIdentifier("teste@agrohub.com.br");
        loginRequest.setPassword("password");
        loginRequest.setUserType(UserType.CLIENTE.name());

        // Configuração padrão da entidade User
        user = new User();
        user.setId(1L);
        user.setEmail("teste@agrohub.com.br");
        user.setSenha("oldHashedPassword");
        user.setTipoUsuario(UserType.CLIENTE);

        // Configuração padrão do CustomUserDetails (que é criado durante a
        // autenticação)
        userDetails = new CustomUserDetails(user);

        // Configuração padrão do objeto Authentication que o AuthenticationManager
        // retorna
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singletonList(new SimpleGrantedAuthority(UserType.CLIENTE.name())));
    }

    // =========================================================================
    // TESTES DE LOGIN (authenticateUser)
    // =========================================================================

    /**
     * Teste para o cenário de login bem-sucedido.
     * Deve retornar um LoginResponseDTO com o token JWT.
     */
    @Test
    void testAuthenticateUser_Success() {
        // ARRANGE: Configuração dos Mocks
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mocked.jwt.token");

        // ACT: Execução do método
        LoginResponseDTO response = authService.authenticateUser(loginRequest);

        // ASSERT: Verificações
        // 1. Deve chamar o authenticationManager
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        // 2. Deve chamar o gerador de token
        verify(jwtTokenProvider, times(1)).generateToken(authentication);

        // 3. Deve retornar o DTO correto
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        // CORREÇÃO APLICADA: Deve esperar o papel com o prefixo ROLE_
        assertThat(response.getUserType()).isEqualTo("ROLE_" + UserType.CLIENTE.name());
        assertThat(response.getMessage()).isEqualTo("Autenticação bem-sucedida");
    }

    /**
     * Teste para o cenário onde as credenciais estão incorretas.
     * Deve lançar InvalidCredentialsException (que mapeia para HTTP 401).
     */
    @Test
    void testAuthenticateUser_InvalidCredentials() {
        // ARRANGE: Configuração dos Mocks para simular falha na autenticação
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Senha incorreta"));

        // ACT & ASSERT: Execução do método e verificação da exceção
        InvalidCredentialsException thrown = assertThrows(InvalidCredentialsException.class, () -> {
            authService.authenticateUser(loginRequest);
        });

        // Verifica a mensagem da exceção
        assertThat(thrown.getMessage()).isEqualTo("Email/CPF/CNPJ ou senha incorretos.");

        // Verifica se o gerador de token NUNCA foi chamado
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    // =========================================================================
    // TESTES DE RESET DE SENHA (resetPassword)
    // =========================================================================

    /**
     * Teste para o cenário de sucesso na redefinição de senha.
     * Deve salvar a nova senha criptografada.
     */
    @Test
    void testResetPassword_Success() {
        // ARRANGE: Configuração dos dados de entrada
        String userEmail = "teste@agrohub.com.br";
        String newPassword = "newPassword123";

        // NOTA: O AuthService atual não valida token, apenas checa se a nova senha foi
        // fornecida.
        PasswordResetRequestDTO request = new PasswordResetRequestDTO(userEmail, "qualquerToken", newPassword);

        // Simulação do usuário encontrado no banco
        User user = new User();
        user.setId(1L);
        user.setSenha("oldHashedPassword");

        // Configuração dos Mocks
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ACT: Execução do método
        authService.resetPassword(request);

        // ASSERT: Verificações
        // 1. Deve buscar o usuário por email
        verify(userRepository, times(1)).findByEmail(userEmail);
        // 2. Deve codificar a nova senha
        verify(passwordEncoder, times(1)).encode(newPassword);
        // 3. Deve salvar o usuário (com a nova senha)
        verify(userRepository, times(1)).save(user);

        // Verificação final do estado: a senha do usuário mockado foi atualizada
        assertThat(user.getSenha()).isEqualTo("newHashedPassword");
    }

    /**
     * Teste para o cenário onde o usuário não é encontrado na redefinição de senha.
     * Deve lançar InvalidCredentialsException.
     */
    @Test
    void testResetPassword_InvalidUser() {
        // ARRANGE: Configuração dos dados de entrada
        String nonExistentEmail = "naoexiste@agrohub.com.br";
        PasswordResetRequestDTO request = new PasswordResetRequestDTO(nonExistentEmail, "token", "newPassword123");

        // Configuração do Mock: Não encontra o usuário
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // ACT & ASSERT: Execução do método e verificação da exceção
        InvalidCredentialsException thrown = assertThrows(InvalidCredentialsException.class, () -> {
            authService.resetPassword(request);
        });

        // Verifica a mensagem da exceção
        assertThat(thrown.getMessage()).isEqualTo("Usuário não encontrado para redefinição.");

        // Verifica se o método save NÃO foi chamado
        verify(userRepository, never()).save(any(User.class));
    }
}