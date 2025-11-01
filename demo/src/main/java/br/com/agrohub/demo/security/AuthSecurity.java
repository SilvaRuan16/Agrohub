package br.com.agrohub.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy; // 👈 IMPORT ADICIONADO
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.TokenResponseDTO;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.UserRepository;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;

@Service
public class AuthSecurity implements UserDetailsService {

    // Não é final, pois será injetado via setter
    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // Construtor sem AuthenticationManager (para quebrar o ciclo inicial)
    public AuthSecurity(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // 🎯 Setter com @Lazy: Resolve o último ciclo de dependência (AuthSecurity <->
    // AuthenticationManager)
    @Autowired
    @Lazy // 👈 CHAVE PARA RESOLVER O CICLO
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Processa a autenticação do usuário e retorna o token JWT.
     */
    public TokenResponseDTO authenticateUser(String username, String password) {
        // O AuthenticationManager é usado aqui.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gera o token a partir do objeto Authentication
        String jwt = tokenProvider.generateToken(authentication);

        // Carrega o usuário para obter o tipo
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return TokenResponseDTO.builder()
                .token(jwt)
                .userType(user.getTipoUsuario().name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(user.getTipoUsuario().name())
                .build();
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User registerNewUser(String email, String cpfCnpj, String rawPassword, UserType userType) {

        User user = new User();
        user.setEmail(email);

        if (userType == UserType.CLIENTE) {
            user.setCpf(cpfCnpj);
        } else {
            user.setCnpj(cpfCnpj);
        }

        user.setTipoUsuario(userType);

        user.setSenha(passwordEncoder.encode(rawPassword));

        return userRepository.save(user);
    }
}