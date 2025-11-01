// JwtAuthenticationFilter.java 

package br.com.agrohub.demo.security.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull; // 👈 1. NOVO IMPORT ADICIONADO AQUI
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // 👈 2. ANOTAÇÃO ADICIONADA AQUI
            @NonNull HttpServletResponse response, // 👈 3. ANOTAÇÃO ADICIONADA AQUI
            @NonNull FilterChain filterChain // 👈 4. ANOTAÇÃO ADICIONADA AQUI
    ) throws ServletException, IOException {

        try {
            // 1. EXTRAI O JWT DO CABEÇALHO 'Authorization'
            String jwt = getJwtFromRequest(request);

            if (jwt != null && tokenProvider.validateToken(jwt)) {

                // 2. O TOKEN É VÁLIDO: EXTRAI O EMAIL
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 3. CARREGA O USUÁRIO E CRIA O OBJETO DE AUTENTICAÇÃO
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 4. DEFINE A AUTENTICAÇÃO NO CONTEXTO DO SPRING SECURITY
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Se o token for inválido, o contexto de segurança é ignorado
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Verifica se o cabeçalho existe e se começa com "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}