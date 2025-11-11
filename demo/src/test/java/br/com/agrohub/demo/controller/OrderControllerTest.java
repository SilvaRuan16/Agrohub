package br.com.agrohub.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService; // 👈 NOVO IMPORT
import org.springframework.security.test.context.support.WithMockUser; // 👈 NOVO IMPORT
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.ItemPedidoRequestDTO;
import br.com.agrohub.demo.dto.OrderRequestDTO;
import br.com.agrohub.demo.dto.OrderResponseDTO;
import br.com.agrohub.demo.security.jwt.JwtTokenProvider; // 👈 NOVO IMPORT
import br.com.agrohub.demo.services.OrderService;

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ActiveProfiles("test")
public class OrderControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private OrderService orderService;

        @MockBean // 👈 ADIÇÃO NECESSÁRIA
        private JwtTokenProvider jwtTokenProvider;

        @MockBean // 👈 ADIÇÃO NECESSÁRIA
        private UserDetailsService userDetailsService;

        private OrderRequestDTO orderRequest;
        private OrderResponseDTO orderResponse;
        private HistoricoPedidoDTO historicoPedido;
        private final Long existingClientId = 1L;
        private final Long nonExistingClientId = 99L;

        @BeforeEach
        void setUp() {
                // 1. Configuração do ItemPedidoRequestDTO (DTO REAL)
                ItemPedidoRequestDTO item1 = new ItemPedidoRequestDTO(101L, 5); // produtoId, quantidade
                ItemPedidoRequestDTO item2 = new ItemPedidoRequestDTO(102L, 10);
                List<ItemPedidoRequestDTO> items = Arrays.asList(item1, item2);

                // 2. Configuração do OrderRequestDTO (DTO REAL)
                // Construtor: (Long clientId, List<ItemPedidoRequestDTO> items, String
                // paymentMethod, Long addressId)
                orderRequest = new OrderRequestDTO(
                                existingClientId,
                                items,
                                "PIX",
                                50L // addressId (Simulação)
                );

                // 3. Configuração do OrderResponseDTO (DTO REAL)
                // Construtor: (Long orderId, BigDecimal totalAmount, String status)
                orderResponse = new OrderResponseDTO(
                                1L,
                                new BigDecimal("1250.50"),
                                "PENDENTE_PAGAMENTO");

                // 4. Configuração do HistoricoPedidoDTO (DTO REAL)
                // Construtor: (Long idPedido, String item, Integer quantity, BigDecimal price,
                // LocalDateTime date, String status)
                historicoPedido = new HistoricoPedidoDTO(
                                200L,
                                "2 itens (Fertilizante, Semente)",
                                2,
                                new BigDecimal("500.00"),
                                LocalDateTime.now().minusDays(3),
                                "ENTREGUE");
        }

        // --- TESTE 1: POST /api/v1/orders (Criação de Pedido) ---

        @Test
        @WithMockUser(username = "client-user", roles = { "CLIENT" }) // 👈 ADIÇÃO DE AUTENTICAÇÃO
        @DisplayName("testCreateOrder_WhenRequestIsValid_ShouldReturn201CreatedAndOrderResponse")
        void testCreateOrder_WhenRequestIsValid_ShouldReturn201CreatedAndOrderResponse() throws Exception {
                // Arrange
                when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(orderResponse);

                // Act & Assert
                mockMvc.perform(post("/api/v1/orders") // Rota POST
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequest)))

                                .andExpect(status().isCreated()) // AGORA DEVE PASSAR COM 201
                                .andExpect(jsonPath("$.orderId", is(orderResponse.getOrderId().intValue())))
                                .andExpect(jsonPath("$.totalAmount", is(orderResponse.getTotalAmount().doubleValue())))
                                .andExpect(jsonPath("$.status", is("PENDENTE_PAGAMENTO")));
        }

        // --- TESTE 2: GET /api/v1/orders/client/{clientId} (Histórico de Pedidos) ---

        @Test
        @WithMockUser(username = "client-user", roles = { "CLIENT" }) // 👈 ADIÇÃO DE AUTENTICAÇÃO
        @DisplayName("testGetClientOrderHistory_WhenHistoryExists_ShouldReturn200OkAndList")
        void testGetClientOrderHistory_WhenHistoryExists_ShouldReturn200OkAndList() throws Exception {
                // Arrange
                List<HistoricoPedidoDTO> history = Collections.singletonList(historicoPedido);
                when(orderService.getClientOrderHistory(eq(existingClientId))).thenReturn(history);

                // Act & Assert
                mockMvc.perform(get("/api/v1/orders/client/{clientId}", existingClientId) // Rota GET
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isOk()) // Espera Status 200 OK
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].idPedido", is(historicoPedido.getIdPedido().intValue())))
                                .andExpect(jsonPath("$[0].status", is("ENTREGUE")));
        }

        @Test
        @WithMockUser(username = "client-user", roles = { "CLIENT" }) // 👈 ADIÇÃO DE AUTENTICAÇÃO
        @DisplayName("testGetClientOrderHistory_WhenClientDoesNotExist_ShouldReturnEmptyList")
        void testGetClientOrderHistory_WhenClientDoesNotExist_ShouldReturnEmptyList() throws Exception {
                // Arrange
                when(orderService.getClientOrderHistory(eq(nonExistingClientId))).thenReturn(Collections.emptyList());

                // Act & Assert
                mockMvc.perform(get("/api/v1/orders/client/{clientId}", nonExistingClientId)
                                .contentType(MediaType.APPLICATION_JSON))

                                .andExpect(status().isOk()) // Espera 200 OK mesmo que a lista seja vazia.
                                .andExpect(jsonPath("$", hasSize(0)));
        }
}