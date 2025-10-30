package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

// DTOs
import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.EnderecoDTO; // <<-- IMPORT NECESSÁRIO
import br.com.agrohub.demo.dto.ComentarioDTO; // <<-- IMPORT ADICIONADO (Para o ProductMapper)

// Entidades (models)
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.ClientAddress; // <<-- IMPORT NECESSÁRIO
import br.com.agrohub.demo.models.ItemPedido; // Para calcular a quantidade no Pedido
import br.com.agrohub.demo.models.Comment; // <<-- IMPORT ADICIONADO (Para o ProductMapper)

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
        // Cálculo de 'quantity' (quantidade total de itens, somando a quantidade de
        // cada ItemPedido)
        int totalQuantity = pedido.getItens() != null
                ? pedido.getItens().stream().mapToInt(ItemPedido::getQuantidade).sum()
                : 0;

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

    /**
     * Mapeia a entidade ClientAddress para o DTO de Endereço.
     */
    public EnderecoDTO toAddressDTO(ClientAddress clientAddress) {
        if (clientAddress == null) {
            return null;
        }

        // Assumindo que a entidade ClientAddress e o DTO EnderecoDTO
        // possuem campos compatíveis. Ajuste o construtor conforme seu EnderecoDTO
        // real.
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

    // ===============================================
    // 🎯 NOVO MÉTODO PARA RESOLVER O ERRO NO ProductMapper 🎯
    // ===============================================
    /**
     * Mapeia a entidade Comment para o DTO de Comentário.
     * OBS: O ProductMapper precisa deste método com esta assinatura exata.
     */
    public ComentarioDTO toComentarioDTO(Comment comment) {
        if (comment == null) {
            return null;
        }

        // Implemente o mapeamento completo aqui.
        // Exemplo:
        // return new ComentarioDTO(comment.getId(), comment.getTexto(),
        // comment.getRating(), ...);

        // Placeholder para compilação. Ajuste conforme os campos do seu ComentarioDTO.
        return new ComentarioDTO();
    }
}