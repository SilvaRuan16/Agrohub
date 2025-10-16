package br.com.agrohub.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Representative;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Long> {

    // Método customizado para buscar um representante pelo seu CPF (identificador único)
    Optional<Representative> findByCpf(String cpf);
    
    // Método customizado para buscar representantes de uma empresa específica
    // Útil para listar todos os contatos de uma Company
    Optional<Representative> findByCompaniesId(Long companyId);
}
