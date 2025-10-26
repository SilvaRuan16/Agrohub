package br.com.agrohub.demo.security;

import br.com.agrohub.demo.dto.TokenResponseDTO;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthSecurity {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Repositórios de Client e Company seriam injetados aqui para o processo de
    // registro
    // private final ClientRepository clientRepository;
    // private final CompanyRepository companyRepository;

    public AuthSecurity(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Processa a autenticação do usuário (Login - p1.png).
     * 
     * @param identifier  O CPF/CNPJ ou Email do usuário.
     * @param rawPassword A senha não codificada.
     * @return TokenResponseDTO contendo o token JWT simulado (ou real, após a
     *         implementação do JWT).
     */
    public TokenResponseDTO authenticateAndGenerateToken(String identifier, String rawPassword) {
        // 1. Tenta autenticar o usuário através do Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, rawPassword));

        // 2. Se a autenticação for bem-sucedida, define-a no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Busca o usuário completo no DB para gerar o TokenResponseDTO
        Optional<User> userOptional = userRepository.findByEmailOrCpfOrCnpj(identifier, identifier, identifier);
        User user = userOptional.orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação."));

        // 4. Retorna o DTO com um token simulado (aqui entraria a geração real do JWT)
        return TokenResponseDTO.builder()
                .token("SIMULATED_JWT_TOKEN_FOR_USER_" + user.getId())
                .id(user.getId())
                .email(user.getEmail())
                .userType(user.getTipoUsuario().name())
                .build();
    }

    /**
     * Verifica se um usuário já existe no sistema pelo email.
     * 
     * @param email O email a ser verificado.
     * @return true se o usuário já existe, false caso contrário.
     */
    public boolean userExists(String email) {
        // Isso pressupõe que UserRepository tem o método existsByEmail(String email)
        return userRepository.existsByEmail(email);
    }

    /**
     * Processa o registro de um novo usuário (p2.png).
     * NOTA: Este é um ESBOÇO. A lógica real usará DTOs específicos e criará
     * Client/Company.
     */
    @Transactional
    public User registerNewUser(String email, String cpfCnpj, String rawPassword, UserType userType) {
        // 1. Verifica se o usuário já existe (por email ou cpf/cnpj)

        // 2. Cria o objeto User
        User user = new User();
        user.setEmail(email);

        if (userType == UserType.CLIENTE) {
            user.setCpf(cpfCnpj);
        } else {
            user.setCnpj(cpfCnpj);
        }

        user.setTipoUsuario(userType);

        // 3. Codifica a senha antes de salvar
        user.setSenha(passwordEncoder.encode(rawPassword));

        // 4. Salva o objeto User
        User savedUser = userRepository.save(user);

        // 5. Lógica de criação de Client ou Company (que usaria
        // clientRepository/companyRepository)
        // ...

        return savedUser;
    }
}
