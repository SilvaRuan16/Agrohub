package br.com.agrohub.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO; // NOVO: DTO de registro
import br.com.agrohub.demo.mappers.ClientMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.ClientAddress;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User; // Para criar o usuário base
import br.com.agrohub.demo.models.UserType; // Para definir o tipo de usuário
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.security.AuthSecurity; // NOVO: Para criar o usuário e criptografar a senha
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela lógica de negócios da Entidade Cliente.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AuthSecurity authSecurity; // NOVO: Injeção para lidar com a criação do User

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, AuthSecurity authSecurity) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.authSecurity = authSecurity;
    }

    /**
     * NOVO MÉTODO: Lógica para registrar um novo cliente.
     * @param requestDTO Os dados do cliente do Front-end.
     */
    @Transactional
    public void registerClient(ClientRegisterRequestDTO requestDTO) {
        
        // 1. CRIAR O USUÁRIO BASE (o registro de senha e autenticação)
        // Isso utiliza o método registerNewUser que já existe no seu AuthSecurity
        User newUser = authSecurity.registerNewUser(
                requestDTO.getEmail(), 
                requestDTO.getCpf(), 
                requestDTO.getSenha(), 
                UserType.CLIENTE
        );
        
        // 2. CRIAR A ENTIDADE CLIENTE
        // Mapeia os dados do DTO para a Entidade Cliente
        Client client = clientMapper.toClient(requestDTO); // Assumindo que você tem um toClient(DTO) no seu ClientMapper
        client.setUser(newUser); // Associa o User recém-criado ao Client
        
        // **3. ADICIONAR LÓGICA COMPLEMENTAR**
        // * Lógica para mapear requestDTO.getEndereco() para Address e ClientAddress
        // * Lógica para mapear requestDTO.getTelefone() para Contact
        // * O ClientMapper precisará ser atualizado para lidar com isso, ou você pode usar Services dedicados.
        
        // 4. SALVAR O CLIENTE
        clientRepository.save(client);
    }
    
    // ... Seu método getClientProfile() continua aqui ...
    
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