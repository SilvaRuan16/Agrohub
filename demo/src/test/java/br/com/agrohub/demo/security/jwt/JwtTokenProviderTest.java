package br.com.agrohub.demo.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    // A classe que estamos testando
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication mockAuthentication;

    // Constantes para um ambiente de teste controlado
    private final String testUsername = "teste@agrohub.com";

    // ATENÇÃO: A chave precisa ter 256 bits (32 bytes)
    // String original: "mySuperSecretKeyForTesting123456" (32 bytes)
    private final String base64TestSecret = "bXlTdXBlclNlY3JldEtleUZvclRlc3RpbmcxMjM0NTY=";

    // 1 hora de expiração para o teste
    private final long testExpirationMs = 3600000;

    @BeforeEach
    void setUp() {
        // Instancia a classe manualmente
        jwtTokenProvider = new JwtTokenProvider();

        // Usa ReflectionTestUtils para injetar os valores @Value
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", base64TestSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", testExpirationMs);
    }

    // =========================================================================
    // TESTES: Geração e Extração (getUsername)
    // =========================================================================

    @Test
    @DisplayName("Deve gerar um token e extrair o username com sucesso")
    void generateToken_And_GetUsernameFromToken_Success() {
        // ARRANGE
        when(mockAuthentication.getName()).thenReturn(testUsername);

        // ACT
        String token = jwtTokenProvider.generateToken(mockAuthentication);
        String usernameFromToken = jwtTokenProvider.getUsernameFromToken(token);

        // ASSERT
        assertNotNull(token);
        assertEquals(testUsername, usernameFromToken);
    }

    // =========================================================================
    // TESTES: validateToken
    // =========================================================================

    @Test
    @DisplayName("Deve validar um token gerado corretamente")
    void validateToken_Success() {
        // ARRANGE
        when(mockAuthentication.getName()).thenReturn(testUsername);
        String validToken = jwtTokenProvider.generateToken(mockAuthentication);

        // ACT
        boolean isValid = jwtTokenProvider.validateToken(validToken);

        // ASSERT
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve falhar na validação de um token expirado")
    void validateToken_Failure_Expired() {
        // ARRANGE
        // Força a expiração para o passado (tempo negativo)
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", -1000L);

        when(mockAuthentication.getName()).thenReturn(testUsername);
        String expiredToken = jwtTokenProvider.generateToken(mockAuthentication);

        // Restaura o tempo de expiração para não afetar outros testes
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", testExpirationMs);

        // ACT
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // ASSERT
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve falhar na validação de um token com assinatura inválida")
    void validateToken_Failure_InvalidSignature() {
        // ARRANGE
        // 1. Gera um token com a chave secreta padrão (Chave A)
        when(mockAuthentication.getName()).thenReturn(testUsername);
        String tokenComChaveA = jwtTokenProvider.generateToken(mockAuthentication);

        // 2. Cria um segundo provider com uma chave secreta DIFERENTE (Chave B)
        JwtTokenProvider providerComChaveB = new JwtTokenProvider();

        // CORREÇÃO: Garante que a chave diferente tenha 32 bytes (256 bits)
        // String nova: "anotherDifferentTestSecret1234XX" (32 bytes)
        String outraChaveBase64 = Base64.getEncoder().encodeToString("anotherDifferentTestSecret1234XX".getBytes());

        // Injeta a chave B e a expiração
        ReflectionTestUtils.setField(providerComChaveB, "jwtSecret", outraChaveBase64);
        ReflectionTestUtils.setField(providerComChaveB, "jwtExpirationMs", testExpirationMs);

        // ACT
        // Tenta validar o token (Chave A) usando o provider (Chave B)
        boolean isValid = providerComChaveB.validateToken(tokenComChaveA);

        // ASSERT
        // Espera-se que a validação falhe devido à SignatureException (agora tratada no
        // código de produção)
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve falhar na validação de um token malformado")
    void validateToken_Failure_Malformed() {
        // ARRANGE
        String malformedToken = "isto.nao.e.um.token";

        // ACT
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        // ASSERT
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve falhar na validação de um token nulo ou vazio")
    void validateToken_Failure_IllegalArgument() {
        // ARRANGE
        String nullToken = null;
        String emptyToken = "";

        // ACT
        boolean isValidNull = jwtTokenProvider.validateToken(nullToken);
        boolean isValidEmpty = jwtTokenProvider.validateToken(emptyToken);

        // ASSERT
        assertFalse(isValidNull);
        assertFalse(isValidEmpty);
    }
}