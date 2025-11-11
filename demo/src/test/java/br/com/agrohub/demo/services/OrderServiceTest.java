package br.com.agrohub.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.ItemPedidoRequestDTO;
import br.com.agrohub.demo.dto.OrderRequestDTO;
import br.com.agrohub.demo.dto.OrderResponseDTO;
import br.com.agrohub.demo.exceptions.ResourceNotFoundException;
import br.com.agrohub.demo.exceptions.ValidationException;
import br.com.agrohub.demo.mappers.CommonMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.Product;
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.repository.ItemPedidoRepository;
import br.com.agrohub.demo.repository.PedidoRepository;
import br.com.agrohub.demo.repository.ProductRepository;

/**
 * Testes unitários para o OrderService.
 * Foco em mockar repositórios e mappers para testar a lógica de negócios de
 * checkout.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    // Mocks para todas as dependências injetadas
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ItemPedidoRepository itemPedidoRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private CommonMapper commonMapper;

    // Entidades e DTOs de simulação
    private Client mockClient;
    private Product mockProduct1;
    private Product mockProduct2;
    private OrderRequestDTO orderRequestDTO;
    private Pedido mockSavedPedido;

    @BeforeEach
    void setUp() {
        // --- 1. Configurar Cliente ---
        mockClient = new Client();
        mockClient.setId(1L);

        // --- 2. Configurar Produtos ---
        mockProduct1 = new Product();
        mockProduct1.setId(10L);
        mockProduct1.setQuantidadeEstoque(100);
        mockProduct1.setPrecoVenda(new BigDecimal("50.00")); // Preço de Venda (Preferencial)

        mockProduct2 = new Product();
        mockProduct2.setId(20L);
        mockProduct2.setQuantidadeEstoque(50);
        mockProduct2.setPrecoVenda(null); // Sem Preço de Venda
        mockProduct2.setPrecoMinVenda(new BigDecimal("30.00")); // Usa Preço Mínimo

        // --- 3. Configurar Requisição (DTO) ---
        ItemPedidoRequestDTO item1 = new ItemPedidoRequestDTO(10L, 2); // 2 * 50.00 = 100.00
        ItemPedidoRequestDTO item2 = new ItemPedidoRequestDTO(20L, 1); // 1 * 30.00 = 30.00
        // Total: 130.00
        orderRequestDTO = new OrderRequestDTO(1L, List.of(item1, item2), "PIX", 1L);

        // --- 4. Configurar Pedido Salvo (Resultado) ---
        mockSavedPedido = new Pedido();
        mockSavedPedido.setId(100L);
        mockSavedPedido.setStatus(Pedido.StatusPedido.PROCESSANDO);
        // Total com 5% de desconto PIX: 130.00 * 0.95 = 123.50
        mockSavedPedido.setValorTotal(new BigDecimal("123.50"));
    }

    // =========================================================================
    // TESTES DE CRIAÇÃO DE PEDIDO (createOrder)
    // =========================================================================

    @Test
    void testCreateOrder_Success_WithPixDiscount() {
        // ARRANGE: Configuração dos Mocks
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(10L)).thenReturn(Optional.of(mockProduct1));
        when(productRepository.findById(20L)).thenReturn(Optional.of(mockProduct2));

        // Simula o save(pedido) retornando o pedido com ID e valor calculado
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(mockSavedPedido);

        // ACT: Execução do método
        OrderResponseDTO response = orderService.createOrder(orderRequestDTO);

        // ASSERT: Verificações
        // 1. Verifica se os repositórios foram chamados
        verify(clientRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(10L);
        verify(productRepository, times(1)).findById(20L);

        // 2. Captura o Pedido que foi salvo
        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository, times(1)).save(pedidoCaptor.capture());
        Pedido capturedPedido = pedidoCaptor.getValue();

        // 3. Verifica o cálculo do valor total (com desconto)
        assertThat(capturedPedido.getValorTotal()).isEqualByComparingTo(new BigDecimal("123.50"));

        // 4. Verifica a baixa de estoque
        assertThat(mockProduct1.getQuantidadeEstoque()).isEqualTo(98); // 100 - 2
        assertThat(mockProduct2.getQuantidadeEstoque()).isEqualTo(49); // 50 - 1

        // 5. Verifica a resposta do DTO
        assertThat(response.getOrderId()).isEqualTo(100L);
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("123.50"));
        assertThat(response.getStatus()).isEqualTo(Pedido.StatusPedido.PROCESSANDO.name());
    }

    @Test
    void testCreateOrder_Success_NoDiscount() {
        // ARRANGE: Muda o método de pagamento para não ter desconto
        orderRequestDTO.setPaymentMethod("CARTAO");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(10L)).thenReturn(Optional.of(mockProduct1));
        when(productRepository.findById(20L)).thenReturn(Optional.of(mockProduct2));

        // Total sem desconto: 130.00
        mockSavedPedido.setValorTotal(new BigDecimal("130.00"));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(mockSavedPedido);

        // ACT
        OrderResponseDTO response = orderService.createOrder(orderRequestDTO);

        // ASSERT
        // Captura o pedido salvo para verificar o valor
        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());
        Pedido capturedPedido = pedidoCaptor.getValue();

        // Verifica o total (sem desconto)
        assertThat(capturedPedido.getValorTotal()).isEqualByComparingTo(new BigDecimal("130.00"));
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("130.00"));
    }

    @Test
    void testCreateOrder_Failure_ClientNotFound() {
        // ARRANGE: Cliente não encontrado
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(orderRequestDTO);
        });

        assertThat(thrown.getMessage()).isEqualTo("Cliente com ID 1 não encontrado(a).");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Failure_ProductNotFound() {
        // ARRANGE: Cliente encontrado, mas Produto 10L não
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(orderRequestDTO);
        });

        assertThat(thrown.getMessage()).isEqualTo("Produto com ID 10 não encontrado(a).");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Failure_InsufficientStock() {
        // ARRANGE: Cliente e produtos encontrados, mas estoque insuficiente
        mockProduct1.setQuantidadeEstoque(1); // Estoque é 1
        orderRequestDTO.getItems().get(0).setQuantidade(2); // Pedido quer 2

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(10L)).thenReturn(Optional.of(mockProduct1));

        // ACT & ASSERT
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            orderService.createOrder(orderRequestDTO);
        });

        assertThat(thrown.getMessage()).isEqualTo("Estoque insuficiente para o produto: 10");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Failure_EmptyOrder() {
        // ARRANGE: Pedido sem itens
        orderRequestDTO.setItems(Collections.emptyList());

        // ACT & ASSERT
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            orderService.createOrder(orderRequestDTO);
        });

        assertThat(thrown.getMessage()).isEqualTo("O pedido não pode estar vazio.");
    }

    // =========================================================================
    // TESTES DE HISTÓRICO (getClientOrderHistory)
    // =========================================================================

    @Test
    void testGetClientOrderHistory_Success() {
        // ARRANGE
        Long clientId = 5L;
        List<Pedido> mockPedidos = List.of(mockSavedPedido); // Lista de pedidos do banco

        // Lista de DTOs que o mapper deve retornar
        List<HistoricoPedidoDTO> mockHistorico = List.of(new HistoricoPedidoDTO(
                100L, "Pedido #100", 2, new BigDecimal("123.50"), LocalDateTime.now(), "PROCESSANDO"));

        when(pedidoRepository.findByClientId(clientId)).thenReturn(mockPedidos);
        when(commonMapper.toHistoricoPedidoDTOList(mockPedidos)).thenReturn(mockHistorico);

        // ACT
        List<HistoricoPedidoDTO> result = orderService.getClientOrderHistory(clientId);

        // ASSERT
        verify(pedidoRepository, times(1)).findByClientId(clientId);
        verify(commonMapper, times(1)).toHistoricoPedidoDTOList(mockPedidos);
        assertThat(result).isEqualTo(mockHistorico);
    }

    // =========================================================================
    // TESTES DE BUSCA (getOrderById)
    // =========================================================================

    @Test
    void testGetOrderById_Success() {
        // ARRANGE
        Long orderId = 100L;
        when(pedidoRepository.findById(orderId)).thenReturn(Optional.of(mockSavedPedido));

        // ACT
        Pedido result = orderService.getOrderById(orderId);

        // ASSERT
        assertThat(result).isEqualTo(mockSavedPedido);
    }

    @Test
    void testGetOrderById_NotFound() {
        // ARRANGE
        Long orderId = 99L;
        when(pedidoRepository.findById(orderId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(orderId);
        });
        assertThat(thrown.getMessage()).isEqualTo("Pedido com ID 99 não encontrado(a).");
    }
}