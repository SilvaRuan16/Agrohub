package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

// DTOs
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.EnderecoDTO; // <<-- IMPORT NECESSÁRIO
// Entidades (models)
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.ClientAddress; // <<-- IMPORT NECESSÁRIO
import br.com.agrohub.demo.models.ItemPedido; // Para calcular a quantidade no Pedido

@Component
public class CommonMapper {

    /**
     * Mapeia uma lista de entidades Pedido para uma lista de DTOs de Histórico.
     */
    public List<HistoricoPedidoDTO> toHistoricoPedidoDTOList(List<Pedido> pedidos) {
        if (pedidos == null) {
            return List.of();
        }
        return pedidos.stream()
            .map(this::toHistoricoPedidoDTO)
            .collect(Collectors.toList());
    }

    /**
     * Mapeia uma única entidade Pedido para o DTO de Histórico.
     */
    public HistoricoPedidoDTO toHistoricoPedidoDTO(Pedido pedido) {
        // Cálculo de 'quantity' (quantidade total de itens, somando a quantidade de cada ItemPedido)
        int totalQuantity = pedido.getItens() != null ? 
            pedido.getItens().stream().mapToInt(ItemPedido::getQuantidade).sum() : 0;
        
        // Simulação do campo 'item' (resumo)
        String itemResumo = "Pedido #" + pedido.getId();
        
        // Criação do DTO com o construtor exato de 6 argumentos
        return new HistoricoPedidoDTO(
            pedido.getId(),
            itemResumo, // String item (resumo do pedido)
            totalQuantity, // Integer quantity (soma das quantidades dos itens)
            pedido.getValorTotal(), // BigDecimal price (valor total)
            pedido.getDataPedido(), // LocalDateTime date (data da compra)
            pedido.getStatus().name() // String status
        );
    }
    
    // 🎯 NOVO MÉTODO QUE RESOLVE O ERRO DE toAddressDTO 🎯
    /**
     * Mapeia a entidade ClientAddress para o DTO de Endereço.
     */
    public EnderecoDTO toAddressDTO(ClientAddress clientAddress) {
        if (clientAddress == null) {
            return null;
        }

        // OBS: Estou assumindo que a entidade ClientAddress e o DTO EnderecoDTO 
        // possuem campos compatíveis para o construtor ou setters. 
        // Como não vi o EnderecoDTO, estou usando uma estrutura comum.
        return new EnderecoDTO(
            clientAddress.getId(),
            clientAddress.getLogradouro(),
            clientAddress.getNumero(),
            clientAddress.getBairro(),
            clientAddress.getCidade(),
            clientAddress.getEstado(),
            clientAddress.getCep(),
            clientAddress.getComplemento(),
            clientAddress.getTipoEndereco() // Assumindo que este campo existe
        );
    }
}