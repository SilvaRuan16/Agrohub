package br.com.agrohub.demo.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    // Constantes e Mocks de suporte
    private final String mockToken = "token.valido.jwt";
    private final String mockUsername = "user@agrohub.com";
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        // Inicializa um mock UserDetails simples
        mockUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(mockUsername)
                .password("ignored")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        // Limpa o contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Limpa o contexto de segurança após cada teste
        SecurityContextHolder.clearContext();
    }

    // =========================================================================
    // TESTES: TOKEN VÁLIDO
    // =========================================================================

    @Test
    @DisplayName("Deve autenticar e definir o contexto de segurança com um token JWT válido")
    void doFilterInternal_ValidToken_Success() throws ServletException, IOException {
        // ARRANGE
        // 1. Simula o cabeçalho Authorization
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        // 2. Simula o Provider validando o token
        when(tokenProvider.validateToken(mockToken)).thenReturn(true);
        // 3. Simula o Provider extraindo o username
        when(tokenProvider.getUsernameFromToken(mockToken)).thenReturn(mockUsername);
        // 4. Simula o UserDetailsService carregando os detalhes do usuário
        when(userDetailsService.loadUserByUsername(mockUsername)).thenReturn(mockUserDetails);

        // ACT
        // A supressão do aviso é a melhor forma de usar mocks não-nulos com @NonNull
        // em um ambiente de teste, ignorando os avisos de Severity 4.
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        // 1. Verifica se a autenticação foi definida no contexto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());

        // 2. CORREÇÃO DA LINHA 106: Removido o terceiro parâmetro (String) para
        // resolver a sobrecarga de métodos.
        // O getPrincipal() retorna Object, mas é garantido que seja UserDetails.
        assertEquals(mockUserDetails, authentication.getPrincipal());

        // 3. Verifica se o filtro continuou a cadeia
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    // =========================================================================
    // TESTES: TOKEN AUSENTE / INVÁLIDO
    // =========================================================================

    @Test
    @DisplayName("Não deve autenticar se o token estiver ausente")
    void doFilterInternal_MissingToken_NoAuth() throws ServletException, IOException {
        // ARRANGE
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        // ACT
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(tokenProvider, never()).validateToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Não deve autenticar se o token não começar com 'Bearer '")
    void doFilterInternal_InvalidHeaderFormat_NoAuth() throws ServletException, IOException {
        // ARRANGE
        when(mockRequest.getHeader("Authorization")).thenReturn("Token " + mockToken);

        // ACT
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(tokenProvider, never()).validateToken(anyString());
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Não deve autenticar se a validação do token falhar")
    void doFilterInternal_InvalidToken_NoAuth() throws ServletException, IOException {
        // ARRANGE
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        when(tokenProvider.validateToken(mockToken)).thenReturn(false);

        // ACT
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(tokenProvider, times(1)).validateToken(mockToken);
        verify(tokenProvider, never()).getUsernameFromToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    // =========================================================================
    // TESTES: TRATAMENTO DE EXCEÇÃO
    // =========================================================================

    @Test
    @DisplayName("Deve ignorar a autenticação e continuar o filtro se o UserDetailsService falhar")
    void doFilterInternal_ExceptionInLoadUserByUsername_ContinuesFilter() throws ServletException, IOException {
        // ARRANGE
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        when(tokenProvider.validateToken(mockToken)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(mockToken)).thenReturn(mockUsername);

        when(userDetailsService.loadUserByUsername(mockUsername))
                .thenThrow(new UsernameNotFoundException("Usuário não encontrado."));

        // ACT
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Deve ignorar a autenticação e continuar o filtro se a extração do username falhar")
    void doFilterInternal_ExceptionInGetUsername_ContinuesFilter() throws ServletException, IOException {
        // ARRANGE
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        when(tokenProvider.validateToken(mockToken)).thenReturn(true);

        when(tokenProvider.getUsernameFromToken(mockToken))
                .thenThrow(new RuntimeException("Falha ao extrair username."));

        // ACT
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // ASSERT
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }
}