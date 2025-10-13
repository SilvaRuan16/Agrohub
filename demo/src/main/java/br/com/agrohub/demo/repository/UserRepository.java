package br.com.agrohub.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Método crucial para o Spring Security (CustomUserDetailsService).
     * Permite buscar o usuário pelo CPF, CNPJ ou Email, pois o campo de login é unificado (p1.png).
     */
    Optional<User> findByEmailOrCpfOrCnpj(String email, String cpf, String cnpj);

    // Método de segurança auxiliar: buscar apenas pelo email (caso o login seja restrito a isso)
    Optional<User> findByEmail(String email);

    // Método de segurança auxiliar: verificar se um CPF já existe no cadastro
    boolean existsByCpf(String cpf);

    // Método de segurança auxiliar: verificar se um CNPJ já existe no cadastro
    boolean existsByCnpj(String cnpj);
}
