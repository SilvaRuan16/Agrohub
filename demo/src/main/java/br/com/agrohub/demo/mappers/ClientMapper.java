package br.com.agrohub.demo.mappers;

// DTOs (Confirmado que estão no pacote raiz 'dto')
import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;

// Entidades (models)
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.models.UserType; // Usando o enum externo
import br.com.agrohub.demo.models.Pedido; // Ainda precisa ser criado
import br.com.agrohub.demo.models.Contact; // Corrigido para seu modelo de Contact

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre a Entidade Client e seus DTOs de
 * Registro e Perfil.
 * Sincronizado com User.senha, User.tipoUsuario, Client.user e Client.contact.
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
        // CORREÇÃO: Usando setSenha() conforme o campo 'senha' em User.java
        user.setSenha(passwordEncoder.encode(dto.getSenha()));

        // CORREÇÃO: Usando setTipoUsuario() conforme o campo 'tipoUsuario' em User.java
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

        // CORREÇÃO: Usando setUser() (Inglês) para o relacionamento com a Entidade User
        client.setUser(user);

        // CORREÇÃO: Usando setContact() (Inglês) para o relacionamento com Contact
        // Chamada adaptada para o seu Contact.java (que tem telefone e email)
        client.setContact(commonMapper.createContactEntity(
                dto.getTelefone(),
                dto.getEmail(),
                null // Assumindo que é o terceiro argumento (ex: redeSocial ou urlSite)
        ));

        return client;
    }

    // =================================================================
    // MAPEAR PARA DTOs (Perfil - Entity to Response)
    // =================================================================

    /**
     * Converte as entidades de Perfil do Cliente para o Response DTO.
     */
    public ClientProfileResponseDTO toClientProfileResponseDTO(User user, Client client, List<Pedido> pedidos,
            List<Address> addresses) {
        if (client == null)
            return null;

        ClientProfileResponseDTO dto = new ClientProfileResponseDTO();

        // 1. DADOS DE USUÁRIO E CLIENTE
        dto.setId(client.getId());
        dto.setEmail(user.getEmail());
        dto.setNomeCompleto(client.getNomeCompleto());
        dto.setCpf(user.getCpf());
        dto.setRg(client.getRg());
        dto.setDataNascimento(client.getDataNascimento());

        // CORREÇÃO: Usando getContact() (Inglês) e acessando o campo 'telefone' do
        // Contact
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

        // AVISO: Este código ainda precisa da classe Pedido.java para compilar.
        return pedidos.stream().map(pedido -> new HistoricoPedidoDTO(
                pedido.getId(),
                "Pedido #" + pedido.getId(),
                pedido.getItens().size(), // Assumindo que Pedido tem o método getItens()
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getStatus().name() // Assumindo que Pedido tem o método getStatus()
        )).collect(Collectors.toList());
    }
}