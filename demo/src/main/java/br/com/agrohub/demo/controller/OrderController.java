package br.com.agrohub.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.HistoricoPedidoDTO;
import br.com.agrohub.demo.dto.OrderRequestDTO;
import br.com.agrohub.demo.dto.OrderResponseDTO;
import br.com.agrohub.demo.services.OrderService;

/**
 * Controller responsável pelo fluxo de Pedidos (Checkout e Histórico de Compras).
 * Roteia as requisições das telas ClientCheckoutScreen.jsx e ClientProfileScreen.jsx.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint: POST /api/v1/orders
     * Cria um novo pedido (Checkout).
     * Roteia a ação do botão 'Confirmar' no ClientCheckoutScreen.jsx.
     * OBS: O ID do cliente viria do token JWT em uma implementação real.
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request) {
        // A lógica complexa de validação, estoque e cálculo está no Service
        OrderResponseDTO response = orderService.createOrder(request);
        
        // Retorna o status 201 Created para indicar a criação de um novo recurso
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint: GET /api/v1/orders/client/{clientId}
     * Busca o histórico de pedidos de um cliente específico.
     * Alimenta a seção de histórico no ClientProfileScreen.jsx.
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<HistoricoPedidoDTO>> getClientOrderHistory(@PathVariable Long clientId) {
        // A lógica de busca e mapeamento está no Service
        List<HistoricoPedidoDTO> history = orderService.getClientOrderHistory(clientId);
        return ResponseEntity.ok(history);
    }
}