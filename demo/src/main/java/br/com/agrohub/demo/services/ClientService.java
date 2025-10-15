package br.com.agrohub.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.mappers.ClientMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.ClientAddress;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela lógica de negócios da Entidade Cliente.
 * Implementa métodos para a tela de Perfil do Cliente (ClientProfileScreen.jsx).
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * Busca o perfil completo de um cliente, incluindo histórico de pedidos.
     * @param userId O ID do usuário logado (obtido via Spring Security Context).
     * @return ClientProfileResponseDTO
     */
    @Transactional(readOnly = true)
    public ClientProfileResponseDTO getClientProfile(Long userId) {
        
        // 1. Buscar o cliente pelo ID do User associado
        Client client = clientRepository.findByUserId(userId)
                             .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o ID de usuário fornecido."));
                             
        // 2. Acessar as listas de Pedidos e Endereços (o @Transactional garante o lazy loading)
        // ESSAS CHAMADAS AGORA FUNCIONAM pois você corrigiu o Client.java!
        List<Pedido> pedidos = client.getPedidos(); 
        List<ClientAddress> enderecos = client.getEnderecos(); // Assumindo que o getter do Client.java se chama getEnderecos()
        
        // 3. Mapear para o DTO de Resposta (incluindo o histórico de pedidos)
        return clientMapper.toClientProfileDTO(client, pedidos, enderecos); 
    }
    
    // ... Aqui entrariam outros métodos do ClientService (ex: updateProfile, addAddress)
}