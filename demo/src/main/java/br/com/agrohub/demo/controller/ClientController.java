package br.com.agrohub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.services.ClientService;

// OBS: Em uma aplicação real, o ID seria extraído do token JWT após o login (Principle).
// Usaremos {clientId} no path por enquanto para fins de teste.

@RestController
@RequestMapping("/api/v1/clients") 
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Endpoint para buscar o perfil completo do cliente (dados, endereços, histórico de pedidos).
     * Mapeia para a tela ClientProfileScreen.jsx.
     * URL: GET /api/v1/clients/{clientId}/profile
     */
    @GetMapping("/{clientId}/profile")
    public ResponseEntity<ClientProfileResponseDTO> getClientProfile(@PathVariable Long clientId) {
        
        try {
            ClientProfileResponseDTO profile = clientService.getClientProfile(clientId);
            return ResponseEntity.ok(profile);
            
        } catch (RuntimeException e) {
            // Em um caso real, você pode ter NotFoundException ou outra exceção customizada
            return ResponseEntity.notFound().build(); 
        }
    }
}