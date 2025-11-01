// JwtTokenProvider.java (Crie no pacote br.com.agrohub.demo.security.jwt)

package br.com.agrohub.demo.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    // 🛑 ATENÇÃO: Use uma chave secreta forte (mínimo 256 bits, ou 32 caracteres)
    @Value("${app.jwtSecret:minhaChaveSecretaParaAssinaturaDeTokensAgroHub}")
    private String jwtSecret;

    // Define o tempo de expiração do token (Ex: 24 horas)
    @Value("${app.jwtExpirationMs:86400000}")
    private long jwtExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Gera o token JWT usando a autenticação (após login).
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username) // O email/identifier do usuário
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrai o nome do usuário (email) do token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida a integridade e a expiração do token.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            // Aqui você pode logar a falha para debug
            return false;
        }
    }
}