package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.ClientAddress;

// Repositório que gerencia o relacionamento N:1 entre Cliente e Endereço.
public interface ClientAddressRepository extends JpaRepository<ClientAddress, Long> {
    
    // Método customizado importante: 
    // Buscar todos os endereços de um cliente específico (usando o ID do cliente).
    List<ClientAddress> findByClientId(Long clientId);
    
    // Buscar o endereço principal (se você tiver uma flag 'isPrincipal' na entidade ClientAddress)
    Optional<ClientAddress> findByClientIdAndIsPrincipalTrue(Long clientId);
}
