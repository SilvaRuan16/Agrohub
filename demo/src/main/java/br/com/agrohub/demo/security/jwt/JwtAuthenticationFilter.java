package br.com.agrohub.demo.security.jwt;

import java.io.IOException;
import java.util.Collection;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    // O UserDetailsService é mantido, mas não é usado no fluxo de autenticação via
    // token
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (jwt != null && tokenProvider.validateToken(jwt)) {

                // EXTRAI O EMAIL/IDENTIFICADOR E AS PERMISSÕES (ROLES) DIRETAMENTE DO TOKEN
                String username = tokenProvider.getUsernameFromToken(jwt);
                Collection<? extends GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromToken(jwt);

                // CRIA O OBJETO DE AUTENTICAÇÃO USANDO O NOME E AS AUTORIDADES DO TOKEN
                // A senha (credentials) é nula, pois a autenticação já foi feita
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities); // <--- CORREÇÃO AQUI

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // DEFINE A AUTENTICAÇÃO NO CONTEXTO DO SPRING SECURITY
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Se o token for inválido, o contexto de segurança é ignorado
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}