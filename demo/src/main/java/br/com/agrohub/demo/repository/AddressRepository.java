package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Address;

// Repositório que gerencia o acesso à tabela 'enderecos' (entidade Address).
// Estende JpaRepository para herdar métodos CRUD prontos.
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // Podemos adicionar métodos customizados aqui se necessário, como:
    // Optional<Address> findByZipCode(String zipCode);
}
