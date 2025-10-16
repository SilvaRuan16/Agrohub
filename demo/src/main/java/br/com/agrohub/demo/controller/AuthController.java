package br.com.agrohub.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.LoginRequestDTO;
import br.com.agrohub.demo.dto.LoginResponseDTO;
import br.com.agrohub.demo.dto.PasswordResetRequestDTO;
import br.com.agrohub.demo.services.AuthService;
// import jakarta.validation.Valid; <--- REMOVIDA

/**
 * Controller responsável pela Autenticação de usuários (Login, Reset de Senha).
 * Roteia as requisições das telas LoginScreen.jsx e ForgotPasswordScreen.jsx.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint: POST /api/v1/auth/login
     * Rota de Login para Cliente e Empresa.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        // A lógica de autenticação está no Service
        LoginResponseDTO response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint: POST /api/v1/auth/reset-password
     * Rota de redefinição de senha (Passo 1: enviar token/link ou Passo 2: atualizar senha).
     * Roteia o componente ForgotPasswordScreen.jsx.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequestDTO resetRequest) {
        authService.resetPassword(resetRequest);
        
        // Retorna 200 OK sem corpo, indicando sucesso na solicitação
        return ResponseEntity.ok().build(); 
    }
}