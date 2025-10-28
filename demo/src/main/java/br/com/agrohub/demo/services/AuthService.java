// Arquivo: AuthService.java
package br.com.agrohub.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.exceptions.InvalidCredentialsException;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType; // Importar
import br.com.agrohub.demo.repository.UserRepository;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injetar o PasswordEncoder

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Processa a autenticação do usuário.
     * CORREÇÃO: Adapta-se ao LoginRequestDTO (identifier, password) e usa
     * PasswordEncoder.
     */
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {

        String identifier = loginRequest.getIdentifier(); // Pode ser CPF, CNPJ ou Email
        UserType requestedType;

        // 1. **Determinar o Tipo de Usuário Solicitado**
        try {
            // Converte a string do DTO para o Enum UserType
            requestedType = UserType.valueOf(loginRequest.getUserType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCredentialsException("Tipo de usuário inválido.");
        }

        // 2. **Buscar o Usuário pelo Identifier**
        Optional<User> userOptional = Optional.empty();

        // Tenta buscar pelo CNPJ ou CPF (depende do UserType e dos campos preenchidos)
        if (requestedType == UserType.EMPRESA) {
            // Se for empresa, tenta buscar por CNPJ
            userOptional = userRepository.findByCnpj(identifier);
        } else if (requestedType == UserType.CLIENTE) {
            // Se for cliente, tenta buscar por CPF
            userOptional = userRepository.findByCpf(identifier);
        }

        // Se não encontrou pelo CPF/CNPJ ou o identifier é um email, tenta buscar por
        // email
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(identifier);
        }

        // Se o usuário ainda não for encontrado, lança a exceção de credenciais
        // inválidas.
        User user = userOptional
                .orElseThrow(() -> new InvalidCredentialsException("Email/CPF/CNPJ ou senha incorretos."));

        // 3. **Verificar se o Tipo de Usuário Logado Corresponde ao Tipo Solicitado**
        if (user.getTipoUsuario() != requestedType) {
            throw new InvalidCredentialsException("Email/CPF/CNPJ ou senha incorretos.");
        }

        // 4. **Verificação da Senha (CORREÇÃO CRÍTICA)**
        // Compara a senha crua do DTO (loginRequest.getPassword()) com a senha
        // codificada do banco (user.getSenha()).
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getSenha())) { // Usa getPassword() do DTO
            throw new InvalidCredentialsException("Email/CPF/CNPJ ou senha incorretos.");
        }

        // 5. Sucesso na Autenticação (Simulação de geração de JWT)
        String mockJwt = "MOCK_JWT_TOKEN_FOR_" + user.getTipoUsuario().name();

        return new LoginResponseDTO(
                mockJwt,
                "Autenticação bem-sucedida",
                user.getTipoUsuario().name());
    }

    /**
     * Lógica de redefinição de senha.
     * CORREÇÃO: Usa o passwordEncoder para codificar a nova senha.
     */
    public void resetPassword(PasswordResetRequestDTO resetRequest) {
        // Assume que o DTO de reset de senha usa 'email' como identificador
        User user = userRepository.findByEmail(resetRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado."));

        if (resetRequest.getNewPassword() != null) {
            // Codifica a nova senha antes de salvar no banco
            user.setSenha(passwordEncoder.encode(resetRequest.getNewPassword()));
            userRepository.save(user);
        }
    }
}