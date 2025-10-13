package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Contact;

// Repositório que gerencia o acesso à tabela 'contatos'.
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Método customizado para buscar contatos por número de telefone
    List<Contact> findByTelefone(String telefone);
    
    // Método customizado para buscar contatos por email
    List<Contact> findByEmail(String email);
    
    // Método customizado para buscar todos os contatos associados a um Client (pelo ID do Client)
    List<Contact> findByClientId(Long clientId);

    // Método customizado para buscar todos os contatos associados a uma Company (pelo ID da Company)
    List<Contact> findByCompanyId(Long companyId);
}
