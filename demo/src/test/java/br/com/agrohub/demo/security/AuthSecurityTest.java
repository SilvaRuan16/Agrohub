package br.com.agrohub.demo.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthSecurityTest {

    @InjectMocks
    private AuthSecurity authSecurity;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication mockAuthentication;

    // Entidades Mockadas
    private User mockUser;
    private final Long userId = 1L;
    private final String userEmail = "teste@agrohub.com";
    private final String userCpf = "12345678900";
    private final String userCnpj = "12345678000199";

    @BeforeEach
    void setUp() {
        // Inicialização do usuário mock
        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail(userEmail);
        mockUser.setSenha("senha_hash");
        mockUser.setTipoUsuario(UserType.CLIENTE);
        // Necessário para simular a lógica do CustomUserDetails (que prioriza CPF/CNPJ)
        mockUser.setCpf(userCpf);
        mockUser.setCnpj(userCnpj);
    }

    // =========================================================================
    // TESTES: loadUserByUsername
    // =========================================================================

    @Test
    @DisplayName("Deve carregar o usuário por Email com sucesso (Retornando CPF, devido à prioridade do CustomUserDetails)")
    void loadUserByUsername_Success_Email() {
        // ARRANGE
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        // ACT
        UserDetails userDetails = authSecurity.loadUserByUsername(userEmail);

        // ASSERT
        assertNotNull(userDetails);
        // O CustomUserDetails prioriza o CPF quando existe.
        assertEquals(userCpf, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userRepository, never()).findByCpf(anyString());
        verify(userRepository, never()).findByCnpj(anyString());
    }

    @Test
    @DisplayName("Deve carregar o usuário por CPF com sucesso")
    void loadUserByUsername_Success_Cpf() {
        // ARRANGE
        // 1. Simula falha na busca por e-mail
        when(userRepository.findByEmail(userCpf)).thenReturn(Optional.empty());
        // 2. Simula sucesso na busca por CPF
        when(userRepository.findByCpf(userCpf)).thenReturn(Optional.of(mockUser));

        // ACT
        UserDetails userDetails = authSecurity.loadUserByUsername(userCpf);

        // ASSERT
        assertNotNull(userDetails);
        // Espera o CPF (o identifier) como o Username
        assertEquals(userCpf, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(userCpf);
        verify(userRepository, times(1)).findByCpf(userCpf);
        verify(userRepository, never()).findByCnpj(anyString());
    }

    @Test
    @DisplayName("Deve carregar o usuário por CNPJ com sucesso")
    void loadUserByUsername_Success_Cnpj() {
        // ARRANGE
        // 1. Simula falha na busca por e-mail
        when(userRepository.findByEmail(userCnpj)).thenReturn(Optional.empty());
        // 2. Simula sucesso na busca por CNPJ
        when(userRepository.findByCnpj(userCnpj)).thenReturn(Optional.of(mockUser));

        // ACT
        UserDetails userDetails = authSecurity.loadUserByUsername(userCnpj);

        // ASSERT
        assertNotNull(userDetails);
        // CORREÇÃO: Espera o CPF (o campo priorizado pelo CustomUserDetails), não o
        // CNPJ
        assertEquals(userCpf, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(userCnpj);
        verify(userRepository, times(1)).findByCnpj(userCnpj);
        verify(userRepository, never()).findByCpf(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado")
    void loadUserByUsername_NotFound() {
        // ARRANGE
        // Usa um identificador longo (Email) para forçar o caminho da busca de CNPJ
        String nonExistingIdentifier = "nao_existe@agrohub.com";
        when(userRepository.findByEmail(nonExistingIdentifier)).thenReturn(Optional.empty());
        // O findByCpf é pulado pela lógica de comprimento da string.
        when(userRepository.findByCnpj(nonExistingIdentifier)).thenReturn(Optional.empty());
        // Removido:
        // when(userRepository.findByCpf(nonExistingIdentifier)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(UsernameNotFoundException.class, () -> {
            authSecurity.loadUserByUsername(nonExistingIdentifier);
        });
        verify(userRepository, times(1)).findByEmail(nonExistingIdentifier);
        // Verifica que o findByCnpj foi chamado (e não o findByCpf)
        verify(userRepository, times(1)).findByCnpj(nonExistingIdentifier);
        verify(userRepository, never()).findByCpf(anyString());
    }

    // =========================================================================
    // TESTES: registerNewUser
    // =========================================================================

    @Test
    @DisplayName("Deve registrar um novo usuário (CLIENTE) com CPF")
    void registerNewUser_Client_Success() {
        // ARRANGE
        String rawPassword = "minhasenha";
        String encodedPassword = "hash_da_senha";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });

        // ACT
        User registeredUser = authSecurity.registerNewUser(userEmail, userCpf, rawPassword, UserType.CLIENTE);

        // ASSERT
        assertNotNull(registeredUser);
        assertEquals(userEmail, registeredUser.getEmail());
        assertEquals(userCpf, registeredUser.getCpf());
        assertEquals(UserType.CLIENTE, registeredUser.getTipoUsuario());
        assertEquals(encodedPassword, registeredUser.getSenha());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve registrar um novo usuário (EMPRESA) com CNPJ")
    void registerNewUser_Company_Success() {
        // ARRANGE
        String rawPassword = "minhasenha";
        String encodedPassword = "hash_da_senha";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });

        // ACT
        User registeredUser = authSecurity.registerNewUser(userEmail, userCnpj, rawPassword, UserType.EMPRESA);

        // ASSERT
        assertNotNull(registeredUser);
        assertEquals(userEmail, registeredUser.getEmail());
        assertEquals(userCnpj, registeredUser.getCnpj());
        assertEquals(UserType.EMPRESA, registeredUser.getTipoUsuario());
        assertEquals(encodedPassword, registeredUser.getSenha());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // =========================================================================
    // TESTES: userExists
    // =========================================================================

    @Test
    @DisplayName("Deve retornar TRUE quando o usuário existe")
    void userExists_True() {
        // ARRANGE
        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        // ACT
        boolean exists = authSecurity.userExists(userEmail);

        // ASSERT
        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail(userEmail);
    }

    @Test
    @DisplayName("Deve retornar FALSE quando o usuário não existe")
    void userExists_False() {
        // ARRANGE
        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        // ACT
        boolean exists = authSecurity.userExists(userEmail);

        // ASSERT
        assertEquals(false, exists);
        verify(userRepository, times(1)).existsByEmail(userEmail);
    }

    // =========================================================================
    // TESTES: getLoggedInUserId
    // =========================================================================

    @Test
    @DisplayName("Deve retornar o ID do usuário logado com sucesso")
    void getLoggedInUserId_Success() {
        // ARRANGE
        // O Authentication.getName() retorna o Username, que neste caso é o CPF
        when(mockAuthentication.getName()).thenReturn(userCpf);
        // O método no service (que você precisa implementar) deve buscar por CPF
        when(userRepository.findByCpf(userCpf)).thenReturn(Optional.of(mockUser));

        // ACT
        Long resultId = authSecurity.getLoggedInUserId(mockAuthentication);

        // ASSERT
        assertEquals(userId, resultId);
        verify(mockAuthentication, times(1)).getName();
        verify(userRepository, times(1)).findByCpf(userCpf);
    }

    @Test
    @DisplayName("Deve lançar exceção se o usuário logado não for encontrado no DB")
    void getLoggedInUserId_NotFound() {
        // ARRANGE
        when(mockAuthentication.getName()).thenReturn(userCpf);
        // Simula que o repositório NÃO encontra o usuário por CPF
        when(userRepository.findByCpf(userCpf)).thenReturn(Optional.empty());

        // ACT & ASSERT
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            authSecurity.getLoggedInUserId(mockAuthentication);
        });

        assertEquals("Usuário logado não encontrado.", thrown.getMessage());
        verify(userRepository, times(1)).findByCpf(userCpf);
    }
}