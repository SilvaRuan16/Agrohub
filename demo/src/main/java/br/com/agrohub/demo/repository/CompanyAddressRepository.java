package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.CompanyAddress;

// Repositório que gerencia o relacionamento N:1 entre Empresa e Endereço.
public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, Long> {
    
    // Método customizado importante: 
    // Buscar todos os endereços de uma empresa específica (usando o ID da empresa).
    List<CompanyAddress> findByCompanyId(Long companyId);
    
    // Buscar o endereço principal (se você tiver uma flag 'isPrincipal' na entidade CompanyAddress)
    Optional<CompanyAddress> findByCompanyIdAndPrincipalTrue(Long companyId);
}
