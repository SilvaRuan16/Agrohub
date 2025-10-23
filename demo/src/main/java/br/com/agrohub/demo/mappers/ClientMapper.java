package br.com.agrohub.demo.mappers;

// DTOs
import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO; 
import br.com.agrohub.demo.dto.ContactDTO; // ⭐ NOVO IMPORT
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;

// Entidades (models)
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.Contact; // ⭐ NOVO IMPORT
import br.com.agrohub.demo.models.ClientAddress;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.ItemPedido; 

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    public ClientMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // 1. MAPEAR PARA ENTIDADE (Registro - Request to Entity)
    // =================================================================

    /**
     * Mapeia ClientRegisterRequestDTO para a Entidade Client (apenas campos diretos).
     * O Service deverá criar e associar as entidades User, Contact e Address.
     */
    public Client toClient(ClientRegisterRequestDTO dto) {
        Client client = new Client();
        
        client.setNomeCompleto(dto.getNomeCompleto());
        client.setRg(dto.getRg());
        client.setCnpj(dto.getCnpj()); // Adicionando CNPJ
        client.setDataNascimento(dto.getDataNascimento());
        
        // ❌ REMOVIDOS: client.setRedeSocial(dto.getRedeSocial()); e client.setWebsite(dto.getWebsite());
        // Esses campos são redundantes no Client e devem ser mapeados via toContact para a entidade Contact.
        
        return client;
    }
    
    /**
     * ⭐ NOVO MÉTODO: Mapeia o DTO aninhado de Contato para a Entidade Contact.
     */
    public Contact toContact(ContactDTO contactDTO) {
        if (contactDTO == null) return null; // Ou lance uma exceção se for obrigatório
        
        Contact contact = new Contact();
        
        contact.setEmail(contactDTO.getEmail());
        contact.setTelefone(contactDTO.getTelefone());
        contact.setRedeSocial(contactDTO.getRedeSocial());
        
        // ⭐ CORREÇÃO DE NOME: Mapeia 'website' do DTO para 'urlSite' da Entidade Contact
        contact.setUrlSite(contactDTO.getWebsite());
        
        return contact;
    }


    // =================================================================
    // 2. MAPEAR PARA DTO (Perfil - Entity to Response)
    // =================================================================

    /**
     * Mapeia Client, Pedidos e Endereços para o DTO de Perfil.
     */
    public ClientProfileResponseDTO toClientProfileDTO(Client client, List<Pedido> pedidos, List<ClientAddress> addresses) {
        
        User user = client.getUser();
        Contact contact = client.getContact(); // Obtendo a entidade Contact associada

        ClientProfileResponseDTO dto = new ClientProfileResponseDTO();

        // 1. DADOS DE USUÁRIO E CLIENTE
        dto.setId(client.getId());
        dto.setEmail(user != null ? user.getEmail() : null); // Email do User
        dto.setNomeCompleto(client.getNomeCompleto());
        dto.setCpf(client.getCpf()); // CPF do Client
        dto.setRg(client.getRg());
        dto.setCnpj(client.getCnpj()); // CNPJ do Client
        dto.setDataNascimento(client.getDataNascimento());
        
        // Mapeando dados do Contact
        if (contact != null) {
            dto.setTelefone(contact.getTelefone()); 
            // Os outros campos de contato (rede social, website) também poderiam ser adicionados aqui se o DTO de resposta exigir.
        }

        // 2. ENDEREÇOS CADASTRADOS
        dto.setEnderecos(addresses.stream()
                .map(commonMapper::toAddressDTO)
                .collect(Collectors.toList()));

        // 3. HISTÓRICO DE COMPRAS (Pedidos)
        dto.setHistoricoPedidos(this.toHistoricoPedidoDTOList(pedidos));

        return dto;
    }

    /**
     * Mapeamento auxiliar do Histórico de Pedidos
     */
    private List<HistoricoPedidoDTO> toHistoricoPedidoDTOList(List<Pedido> pedidos) {
        if (pedidos == null)
            return List.of();

        return pedidos.stream().map(pedido -> {
            
            // Usando ItemPedido para calcular a quantidade
            Integer quantidadeItens = pedido.getItens().stream()
                .mapToInt(ItemPedido::getQuantidade) 
                .sum(); 

            // Construção do DTO
            return new HistoricoPedidoDTO(
                pedido.getId(),
                "Pedido #" + pedido.getId(), 
                quantidadeItens, 
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getStatus().name() 
            );
        }).collect(Collectors.toList());
    }
}