package br.com.agrohub.demo.services;

import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.exceptions.InvalidCredentialsException;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.repository.UserRepository;

/**
 * Service para lidar com a lógica de Autenticação (Login, Logout, Reset de
 * Senha).
 * NOTA: O Token JWT real e o SecurityContext devem ser implementados para um
 * ambiente de produção.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    // NOTE: O AuthenticationManager é essencial em projetos Spring Security reais
    // private final AuthenticationManager authenticationManager;
    // private final JwtTokenProvider jwtTokenProvider;

    // Ajuste o construtor conforme as dependências que você adicionar
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Processa a autenticação do usuário.
     */
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        // --- SIMULAÇÃO DA AUTENTICAÇÃO ---

        // Em um projeto real, você usaria o AuthenticationManager:
        /*
         * Authentication authentication = authenticationManager.authenticate(
         * new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
         * loginRequest.getPassword()));
         * 
         * String jwt = jwtTokenProvider.generateToken(authentication);
         * UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         * User user = (User) userDetails;
         */

        // Simulação de busca por email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha incorretos."));

        // Simulação de verificação de senha (o bcrypt real deve ser usado)
        // if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        // { ... }
        if (!user.getSenha().equals(loginRequest.getSenha())) { // CORRIGIDO: Usa getSenha() do DTO
            throw new InvalidCredentialsException("Email ou senha incorretos.");
        }

        // Simulação de geração de JWT e tipo de usuário
        String mockJwt = "MOCK_JWT_TOKEN_FOR_" + user.getTipoUsuario().name();

        return new LoginResponseDTO(
                mockJwt,
                "Autenticação bem-sucedida",
                user.getTipoUsuario().name() // Retorna 'CLIENTE' ou 'EMPRESA'
        );
    }

    /**
     * Inicia/finaliza o processo de reset de senha.
     */
    public void resetPassword(PasswordResetRequestDTO resetRequest) {
        // 1. Validar se o usuário existe (pelo email)
        User user = userRepository.findByEmail(resetRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado."));

        // 2. Lógica de redefinição real (ex: Enviar código/link por email)
        // No nosso caso, como é apenas um mock para ligar o Front, vamos simular a
        // atualização direta

        if (resetRequest.getNewPassword() != null) {
            // Simulação de atualização de senha
            // user.setSenha(passwordEncoder.encode(resetRequest.getNewPassword()));
            user.setSenha(resetRequest.getNewPassword()); // APENAS PARA MOCK
            userRepository.save(user);
        }

        // Em produção, a lógica aqui é mais complexa.
    }
}