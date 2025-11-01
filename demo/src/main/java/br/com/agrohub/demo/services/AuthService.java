package br.com.agrohub.demo.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.exceptions.InvalidCredentialsException;
import br.com.agrohub.demo.models.User; // Importação necessária para o método resetPassword
import br.com.agrohub.demo.repository.UserRepository;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider;
import br.com.agrohub.demo.security.CustomUserDetails;

/**
 * Service de Autenticação.
 * Responsável por gerenciar o login e redefinição de senha,
 * utilizando o AuthenticationManager do Spring Security para o fluxo de
 * autenticação.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * CONSTRUTOR ATUALIZADO
     * Injeta todas as dependências necessárias, incluindo os componentes de
     * segurança.
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Processa a autenticação do usuário, agora usando o AuthenticationManager.
     * 
     * @param loginRequest DTO com identifier (CPF/CNPJ/Email) e senha.
     * @return DTO contendo o JWT, mensagem e o tipo de usuário.
     * @throws InvalidCredentialsException se a autenticação falhar.
     */
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {

        // 1. Cria o token de autenticação (sem estar autenticado)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                // O identificador DEVE coincidir com o que o seu UserDetailsService usa
                loginRequest.getIdentifier(),
                loginRequest.getPassword());

        try {
            // 2. Tenta autenticar usando o Spring Security.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. Se autenticação for bem-sucedida, recupera os detalhes do usuário
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 4. Gerar o JWT (usando o serviço de token)
            String jwt = tokenProvider.generateToken(authentication);

            // 5. Recuperar o tipo de usuário (Role, que deve ser CLIENTE ou EMPRESA em
            // MAIÚSCULAS)
            String userType = userDetails.getAuthorities().stream()
                    .findFirst() // Pega a primeira autoridade/Role
                    .orElseThrow(() -> new InvalidCredentialsException("Tipo de usuário não definido."))
                    .getAuthority();

            // 6. Retornar a resposta de sucesso
            return new LoginResponseDTO(
                    jwt,
                    "Autenticação bem-sucedida",
                    userType);

        } catch (AuthenticationException e) {
            // Captura qualquer erro de autenticação e lança a exceção padronizada (Status
            // 401).
            throw new InvalidCredentialsException("Email/CPF/CNPJ ou senha incorretos.");
        }
    }

    /**
     * Lógica de redefinição de senha.
     * Usa o passwordEncoder para codificar a nova senha antes de salvar.
     */
    public void resetPassword(PasswordResetRequestDTO resetRequest) {

        User user = userRepository.findByEmail(resetRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado para redefinição."));

        if (resetRequest.getNewPassword() != null) {
            // Codifica a nova senha antes de salvar no banco
            user.setSenha(passwordEncoder.encode(resetRequest.getNewPassword()));
            userRepository.save(user);
        }
    }
}