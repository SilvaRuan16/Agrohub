package br.com.agrohub.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO; 
import br.com.agrohub.demo.mappers.ClientMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.ClientAddress;
import br.com.agrohub.demo.models.Contact; // NOVO: Para lidar com a entidade de Contato
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User; 
import br.com.agrohub.demo.models.UserType; 
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.repository.ContactRepository; // ⭐ NOVO: Repositório para salvar Contato
import br.com.agrohub.demo.security.AuthSecurity; 
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela lógica de negócios da Entidade Cliente.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository; // ⭐ NOVO: Repositório injetado
    private final ClientMapper clientMapper;
    private final AuthSecurity authSecurity; 

    // CONSTRUTOR ATUALIZADO PARA INCLUIR ContactRepository
    public ClientService(ClientRepository clientRepository, ContactRepository contactRepository, ClientMapper clientMapper, AuthSecurity authSecurity) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
        this.clientMapper = clientMapper;
        this.authSecurity = authSecurity;
    }

    /**
     * Lógica para registrar um novo cliente.
     * @param requestDTO Os dados do cliente do Front-end.
     */
    @Transactional
    public void registerClient(ClientRegisterRequestDTO requestDTO) {
        
        // 1. CRIAR E SALVAR A ENTIDADE CONTATO
        // Mapeia o DTO aninhado (ContactDTO) para a Entidade Contact
        Contact contact = clientMapper.toContact(requestDTO.getContact());
        // SALVAR ANTES: A entidade Client tem uma chave estrangeira NOT NULL para Contact
        contact = contactRepository.save(contact); 

        // 2. CRIAR O USUÁRIO BASE
        // CORREÇÃO: Acessa o email através do objeto aninhado 'contact'
        User newUser = authSecurity.registerNewUser(
                requestDTO.getContact().getEmail(), // ⭐ CORREÇÃO APLICADA AQUI
                requestDTO.getCpf(), 
                requestDTO.getSenha(), 
                UserType.CLIENTE
        );
        
        // 3. CRIAR A ENTIDADE CLIENTE
        Client client = clientMapper.toClient(requestDTO); 
        
        // Associa as entidades recém-criadas ao Cliente
        client.setUser(newUser);      // Associa o User
        client.setContact(contact);   // Associa o Contato salvo

        // 4. SALVAR O CLIENTE
        clientRepository.save(client);
        
        // 5. LÓGICA COMPLEMENTAR (Endereço, se for o caso)
    }
    
    /**
     * Busca o perfil completo do cliente pelo ID do Usuário.
     */
    @Transactional(readOnly = true)
    public ClientProfileResponseDTO getClientProfile(Long userId) {
        
        // 1. Buscar o cliente pelo ID do User associado
        Client client = clientRepository.findByUserId(userId)
                                     .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o ID de usuário fornecido."));
                                     
        // 2. Acessar as listas de Pedidos e Endereços (o @Transactional garante o lazy loading)
        List<Pedido> pedidos = client.getPedidos(); 
        List<ClientAddress> enderecos = client.getEnderecos(); 
        
        // 3. Mapear para o DTO de Resposta (incluindo o histórico de pedidos)
        return clientMapper.toClientProfileDTO(client, pedidos, enderecos); 
    }
}