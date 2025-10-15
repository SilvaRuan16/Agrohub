package br.com.agrohub.demo.mappers;

// DTOs
import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.EnderecoDTO; // Importar o DTO de Endereço

// Entidades (models)
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.ClientAddress; // 🎯 ESSENCIAL: Sua entidade de endereço
import br.com.agrohub.demo.models.UserType; 
import br.com.agrohub.demo.models.Pedido; 
import br.com.agrohub.demo.models.Contact; 
import br.com.agrohub.demo.models.ItemPedido; // ESSENCIAL: Para calcular a quantidade no histórico

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal; 
import java.time.LocalDateTime; 

@Component
public class ClientMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    public ClientMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // 2. MAPEAR PARA DTO (Perfil - Entity to Response)
    // =================================================================

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