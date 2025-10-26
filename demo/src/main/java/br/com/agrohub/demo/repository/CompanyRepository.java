package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Company;

// Repositório que gerencia o acesso à tabela 'empresas' (entidade Company).
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Método customizado crucial para o cadastro e autenticação:
    // Busca empresa pelo CNPJ (o identificador principal)
    Optional<Company> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

    // Busca empresa pela Razão Social (útil para pesquisa administrativa ou
    // display)
    Optional<Company> findByRazaoSocial(String razaoSocial);

    // Buscar empresa pelo ID do User associado (útil após o login para carregar o
    // perfil)
    Optional<Company> findByUserId(Long userId);
}
