package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO; // 1. Importar o DTO de requisição
import br.com.agrohub.demo.services.ClientService;
import jakarta.validation.Valid; // Se estiver usando validação Bean Validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // 2. Importar o PostMapping
import org.springframework.web.bind.annotation.RequestBody; // 3. Importar o RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients") // Base URL: /api/v1/clients
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // NOVO MÉTODO: Endpoint para o registro de clientes
    /**
     * Endpoint para registrar um novo cliente.
     * URL: POST /api/v1/clients/register
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerClient(@Valid @RequestBody ClientRegisterRequestDTO requestDTO) {
        // Você precisará criar o método registerClient no seu ClientService
        clientService.registerClient(requestDTO); 
        
        // Retorna 201 Created (ou 200 OK, dependendo da sua preferência)
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    // ... Seu método getClientProfile() continua aqui
    @GetMapping("/{clientId}/profile")
    public ResponseEntity<ClientProfileResponseDTO> getClientProfile(@PathVariable Long clientId) {
        // ... (resto do código)
        try {
            ClientProfileResponseDTO profile = clientService.getClientProfile(clientId);
            return ResponseEntity.ok(profile);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }
}