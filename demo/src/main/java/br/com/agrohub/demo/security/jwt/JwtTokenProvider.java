package br.com.agrohub.demo.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret:minhaChaveSecretaParaAssinaturaDeTokensAgroHub}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:86400000}")
    private long jwtExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Gera o token JWT, incluindo o 'username' e a claim 'roles' (permissões).
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // EXTRAI AS PERMISSÕES (ROLES)
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // <<--- CORREÇÃO AQUI
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrai o nome do usuário (email/identifier) do token.
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
     * Extrai as permissões (roles) do token JWT.
     * NOVO MÉTODO: Essencial para o JwtAuthenticationFilter.
     */
    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String rolesString = claims.get("roles", String.class);

            if (rolesString == null || rolesString.isEmpty()) {
                return List.of();
            }

            return Arrays.stream(rolesString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (JwtException ex) {
            return List.of();
        }
    }

    /**
     * Valida a integridade e a expiração do token.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException ex) {
            return false;
        }
    }
}