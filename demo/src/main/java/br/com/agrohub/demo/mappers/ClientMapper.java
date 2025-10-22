package br.com.agrohub.demo.mappers;

// DTOs
import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO; // NOVO: DTO de registro
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;

// Entidades (models)
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.User;
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
     * NOVO MÉTODO: Mapeia ClientRegisterRequestDTO para a Entidade Client.
     * ATENÇÃO: Campos de User (email, cpf, senha) são mapeados no AuthSecurity/ClientService.
     * ATENÇÃO: Campos de Address/Contact precisam de mappers/lógica adicionais no Service.
     */
    public Client toClient(ClientRegisterRequestDTO dto) {
        Client client = new Client();
        
        client.setNomeCompleto(dto.getNomeCompleto());
        client.setRg(dto.getRg());
        client.setDataNascimento(dto.getDataNascimento());
        client.setRedeSocial(dto.getRedeSocial());
        client.setWebsite(dto.getWebsite());
        // Obs: O ID do User será setado no ClientService.java
        
        return client;
    }


    // =================================================================
    // 2. MAPEAR PARA DTO (Perfil - Entity to Response)
    // =================================================================

    // ... (O restante do seu código toClientProfileDTO e toHistoricoPedidoDTOList continua igual) ...
    /**
     * Mapeia Client, Pedidos e Endereços para o DTO de Perfil.
     * Assinatura Correta: List<ClientAddress> addresses
     */
    public ClientProfileResponseDTO toClientProfileDTO(Client client, List<Pedido> pedidos, List<ClientAddress> addresses) {
        
        User user = client.getUser();

        ClientProfileResponseDTO dto = new ClientProfileResponseDTO();

        // 1. DADOS DE USUÁRIO E CLIENTE
        dto.setId(client.getId());
        dto.setEmail(user.getEmail());
        dto.setNomeCompleto(client.getNomeCompleto());
        dto.setCpf(user.getCpf()); 
        dto.setRg(client.getRg());
        dto.setDataNascimento(client.getDataNascimento());
        dto.setTelefone(client.getContact() != null ? client.getContact().getTelefone() : null); 

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