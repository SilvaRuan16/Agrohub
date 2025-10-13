package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Client;

// Repositório que gerencia o acesso à tabela 'clientes' (entidade Client).
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    // Método customizado importante para o cadastro/login:
    // Buscar cliente pelo CPF
    Optional<Client> findByCpf(String cpf);

    // Método customizado importante para o cadastro/login (caso seja PJ):
    // Buscar cliente pelo CNPJ
    Optional<Client> findByCnpj(String cnpj);
    
    // Buscar cliente pelo ID do User associado (útil após o login)
    Optional<Client> findByUserId(Long userId);
}
