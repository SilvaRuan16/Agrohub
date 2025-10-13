package br.com.agrohub.demo.mappers;

// CORREÇÃO: Adicionado o subpacote 'user'
import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;

// Entidades (models)
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.Address;    // Adicionado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre a Entidade Client e seus DTOs de Registro e Perfil.
 * Utiliza CommonMapper para Address e Contact.
 */
@Component
public class ClientMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // MAPEAR PARA ENTIDADES (Registro - Request to Entity)
    // =================================================================

    /**
     * Cria a entidade User a partir do DTO de Registro.
     */
    public User toUserEntity(ClientRegisterRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setPassword(passwordEncoder.encode(dto.getSenha())); 
        // Corrigir o UserType de acordo com o nome da variável na sua classe User
        user.setTipoUsuario(UserType.CLIENTE); 
        return user;
    }

    /**
     * Cria a entidade Client a partir do DTO de Registro, associando-o ao User.
     */
    public Client toClientEntity(ClientRegisterRequestDTO dto, User user) {
        Client client = new Client();
        client.setNomeCompleto(dto.getNomeCompleto());
        client.setRg(dto.getRg());
        client.setDataNascimento(dto.getDataNascimento());
        client.setUsuario(user); 
        
        // Usa o método corrigido do CommonMapper que cria a entidade Contact
        client.setContato(commonMapper.createContactEntity(dto.getTelefone(), null, dto.getEmail()));
        
        return client;
    }


    // =================================================================
    // MAPEAR PARA DTOs (Perfil - Entity to Response)
    // =================================================================

    /**
     * Converte as entidades de Perfil do Cliente para o Response DTO.
     */
    public ClientProfileResponseDTO toClientProfileResponseDTO(User user, Client client, List<Pedido> pedidos, List<Address> addresses) {
        if (client == null) return null;

        ClientProfileResponseDTO dto = new ClientProfileResponseDTO();
        
        // 1. DADOS DE USUÁRIO E CLIENTE
        dto.setId(client.getId());
        dto.setEmail(user.getEmail());
        dto.setNomeCompleto(client.getNomeCompleto());
        dto.setCpf(user.getCpf());
        dto.setRg(client.getRg());
        dto.setDataNascimento(client.getDataNascimento());
        dto.setTelefone(client.getContato() != null ? client.getContato().getPhone() : null);

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
        if (pedidos == null) return List.of();
        
        return pedidos.stream().map(pedido -> new HistoricoPedidoDTO(
            pedido.getId(),
            "Pedido #" + pedido.getId(), 
            pedido.getItens().size(), 
            pedido.getValorTotal(),
            pedido.getDataPedido(),
            pedido.getStatus().name()
        )).collect(Collectors.toList());
    }
}