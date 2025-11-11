package br.com.agrohub.demo.security;

import org.springframework.security.core.Authentication; // NOVO IMPORT NECESSÁRIO
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class AuthSecurity implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 

    public AuthSecurity(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * loadUserByUsername: O CORAÇÃO DO LOGIN.
     * O Spring Security (AuthenticationManager) chama este método com o identifier.
     * * @param identifier O CPF, CNPJ ou Email fornecido na tela de login.
     * @return Uma instância de CustomUserDetails.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // 1. Tenta buscar o usuário por diferentes campos
        Optional<User> userOptional = Optional.empty();

        // Tentamos buscar por e-mail
        userOptional = userRepository.findByEmail(identifier);

        // Se não for e-mail, tentamos CPF (se o tamanho for compatível)
        if (userOptional.isEmpty() && identifier.length() <= 11) {
            userOptional = userRepository.findByCpf(identifier);
        }
        // Se não for e-mail nem CPF, tentamos CNPJ (se o tamanho for compatível)
        else if (userOptional.isEmpty() && identifier.length() > 11) {
            userOptional = userRepository.findByCnpj(identifier);
        }

        // Se o usuário não for encontrado
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + identifier));

        // 2. Retorna o objeto CustomUserDetails, que o Spring usará para comparar a
        // senha.
        return new CustomUserDetails(user);
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

        // HASH da senha
        user.setSenha(passwordEncoder.encode(rawPassword));

        return userRepository.save(user);
    }
    
    /**
     * Extrai o ID do usuário (Primary Key Long) a partir do objeto Authentication.
     * É usado por serviços que precisam saber o ID do usuário logado (ex: ProductService).
     * @param authentication Objeto de autenticação do Spring Security.
     * @return ID do Usuário (Long).
     */
    public Long getLoggedInUserId(Authentication authentication) {
        // O getName() retorna o Username configurado no CustomUserDetails (Email, CPF ou CNPJ)
        String identifier = authentication.getName();
        
        // 1. Tenta buscar o usuário por diferentes campos, usando a mesma lógica do loadUserByUsername
        Optional<User> userOptional = Optional.empty();

        // Tentamos buscar por e-mail
        userOptional = userRepository.findByEmail(identifier);

        // Se não for e-mail, tentamos CPF (se o tamanho for compatível)
        if (userOptional.isEmpty() && identifier.length() <= 11) {
            userOptional = userRepository.findByCpf(identifier);
        }
        // Se não for e-mail nem CPF, tentamos CNPJ (se o tamanho for compatível)
        else if (userOptional.isEmpty() && identifier.length() > 11) {
            userOptional = userRepository.findByCnpj(identifier);
        }
        
        // Se o usuário não for encontrado (geralmente não deve acontecer para um usuário logado)
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuário logado não encontrado."));
        
        // Retorna o ID
        return user.getId();
    }
}