package br.com.agrohub.demo.mappers;

// DTOs
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component; // Certifique-se de que EnderecoDTO existe

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.Contact;
import br.com.agrohub.demo.models.Pedido; // Adicionado para Contact.java
import br.com.agrohub.demo.models.User;

/**
 * Mapper responsável pela conversão entre a Entidade Client e seus DTOs de
 * Registro e Perfil.
 */
@Component
public class ClientMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    public ClientMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // 1. MAPEAR PARA ENTIDADES (Registro - Request to Entity)
    // =================================================================

    // Implementação do toUserEntity e toClientEntity... (Deixando de fora por hora)

    // =================================================================
    // 2. MAPEAR PARA DTO (Perfil - Entity to Response)
    // =================================================================

    /**
     * Mapeia Client, Pedidos e Endereços para o DTO de Perfil.
     * Método usado pelo ClientService.
     */
    public ClientProfileResponseDTO toClientProfileDTO(Client client, List<Pedido> pedidos, List<Address> addresses) {
        // Obter objetos necessários
        User user = client.getUser();
        Contact contact = client.getContact();

        ClientProfileResponseDTO dto = new ClientProfileResponseDTO();

        // 1. DADOS DE USUÁRIO E CLIENTE
        dto.setId(client.getId());
        dto.setEmail(user.getEmail());
        dto.setNomeCompleto(client.getNomeCompleto());
        dto.setCpf(user.getCpf()); // CPF vem do User
        dto.setRg(client.getRg());
        dto.setDataNascimento(client.getDataNascimento());

        // Telefone vem da entidade Contact
        dto.setTelefone(contact != null ? contact.getTelefone() : null);

        // 2. ENDEREÇOS CADASTRADOS
        // Assumindo que commonMapper.toAddressDTO mapeia Address para EnderecoDTO
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
            // Lógica para obter o nome do item principal (ou um resumo)
            String itemResumo = "Pedido #" + pedido.getId();
            
            // O ItemPedido.java tem o campo 'quantidade'. 
            // Somamos as quantidades dos itens dentro do pedido.
            Integer quantidadeItens = pedido.getItens().stream()
                .mapToInt(item -> item.getQuantidade()) // Mapeia para a quantidade
                .sum(); // Soma todas as quantidades

            return new HistoricoPedidoDTO(
                pedido.getId(),
                itemResumo, // Ex: "Pedido #123"
                quantidadeItens, // Total de unidades compradas (quantidade agregada)
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getStatus().name() // Ex: "EM_TRANSPORTE", "ENTREGUE"
            );
        }).collect(Collectors.toList());
    }
}