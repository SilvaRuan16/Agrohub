package br.com.agrohub.demo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.ItemPedidoRequestDTO;
import br.com.agrohub.demo.dto.OrderRequestDTO;
import br.com.agrohub.demo.dto.OrderResponseDTO;
import br.com.agrohub.demo.exceptions.ResourceNotFoundException;
import br.com.agrohub.demo.exceptions.ValidationException;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.ItemPedido;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.repository.ItemPedidoRepository;
import br.com.agrohub.demo.repository.PedidoRepository;
import br.com.agrohub.demo.repository.ProductRepository;
import br.com.agrohub.demo.mappers.CommonMapper; // Assumindo a existência de um CommonMapper

@Service
public class OrderService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    // O CommonMapper deve ser usado para mapear a Entidade Pedido para o DTO de Histórico
    private final CommonMapper commonMapper; 

    public OrderService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository,
                        ProductRepository productRepository, ClientRepository clientRepository, CommonMapper commonMapper) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.commonMapper = commonMapper;
    }

    /**
     * 1. Cria um novo Pedido e seus Itens, simulando o checkout.
     */
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("O pedido não pode estar vazio.");
        }

        // 1. Encontrar o Cliente
        // OBS: clientRepository e a entidade Client devem existir no seu projeto.
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getClientId()));

        // 2. Criar e configurar o Pedido principal
        Pedido pedido = new Pedido();
        pedido.setClient(client);
        pedido.setDataPedido(LocalDateTime.now());
        // OBS: A classe Pedido deve ter o ENUM StatusPedido.
        pedido.setStatus(Pedido.StatusPedido.PROCESSANDO); 

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<ItemPedido> itens = new ArrayList<>();

        // 3. Processar cada item do carrinho
        for (ItemPedidoRequestDTO itemRequest : request.getItems()) {
            // productRepository deve retornar a entidade Product.
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", itemRequest.getProductId()));

            if (itemRequest.getQuantidade() <= 0) {
                throw new ValidationException("Quantidade inválida para o produto " + product.getId());
            }

            // 🎯 CORREÇÃO APLICADA: Usando getQuantidadeEstoque()
            if (product.getQuantidadeEstoque() < itemRequest.getQuantidade()) {
                throw new ValidationException("Estoque insuficiente para o produto: " + product.getId());
            }

            // 4. Criar o ItemPedido
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido); // Associa ao pedido
            itemPedido.setProduct(product);
            itemPedido.setQuantidade(itemRequest.getQuantidade());
            // OBS: A entidade Product deve ter o campo precoVenda ou currentPrice.
            itemPedido.setPrecoUnitario(product.getPrecoVenda() != null ? product.getPrecoVenda() : product.getPrecoMinVenda()); 

            // Calcula o subtotal do item
            BigDecimal subtotal = itemPedido.getPrecoUnitario().multiply(new BigDecimal(itemRequest.getQuantidade()));
            totalAmount = totalAmount.add(subtotal);

            itens.add(itemPedido);
            
            // Simulação de baixa de estoque (em um projeto real, salvaria o Product aqui)
            // 🎯 CORREÇÃO APLICADA: Usando setQuantidadeEstoque()
            product.setQuantidadeEstoque(product.getQuantidadeEstoque() - itemRequest.getQuantidade());
        }
        
        // 5. Aplicar desconto (Ex: PIX 5%)
        if ("PIX".equalsIgnoreCase(request.getPaymentMethod())) {
            BigDecimal discountRate = new BigDecimal("0.05"); 
            BigDecimal discount = totalAmount.multiply(discountRate);
            totalAmount = totalAmount.subtract(discount);
        }

        // 6. Finalizar e Salvar o Pedido
        pedido.setValorTotal(totalAmount);
        pedido.setItens(itens); 
        
        // OBS: Assumindo que Pedido tem o relacionamento OneToMany com Itens e o CascadeType.ALL.
        Pedido savedPedido = pedidoRepository.save(pedido);

        // 7. Retornar a resposta
        return new OrderResponseDTO(
            savedPedido.getId(),
            savedPedido.getValorTotal(),
            savedPedido.getStatus().name()
        );
    }

    /**
     * 2. Busca o histórico de pedidos de um cliente.
     */
    public List<HistoricoPedidoDTO> getClientOrderHistory(Long clientId) {
        // 1. Busca os pedidos
        List<Pedido> pedidos = pedidoRepository.findByClientId(clientId);

        // 2. Mapeia para DTO
        // OBS: Se CommonMapper.toHistoricoPedidoDTOList não existir, isso causará um erro
        // de "cannot find symbol". A classe HistoricoPedidoDTO também é necessária.
        return commonMapper.toHistoricoPedidoDTOList(pedidos); 
    }
    
    /**
     * 3. Busca um pedido por ID.
     */
    public Pedido getOrderById(Long orderId) {
        return pedidoRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", orderId));
    }
}