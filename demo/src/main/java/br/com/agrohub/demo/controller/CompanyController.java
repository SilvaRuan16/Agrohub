package br.com.agrohub.demo.controller;

import java.nio.file.AccessDeniedException; // Import adicionado
import java.util.List; // Import adicionado

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Import adicionado
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO; // Import adicionado
import br.com.agrohub.demo.services.CompanyService;
import br.com.agrohub.demo.services.ProductService; // Import adicionado
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final ProductService productService; // Campo adicionado

    @Autowired
    public CompanyController(CompanyService companyService, ProductService productService) { // Construtor ajustado
        this.companyService = companyService;
        this.productService = productService; // Injeção de ProductService
    }

    /**
     * Endpoint: POST /api/v1/companies/register
     * Registra uma nova empresa.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerCompany(@Valid @RequestBody CompanyRegisterRequestDTO requestDTO) {
        companyService.registerCompany(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint: GET /api/v1/companies/{companyId}/profile
     * Busca o perfil público de uma empresa pelo ID.
     */
    @GetMapping("/{companyId}/profile")
    public ResponseEntity<CompanyProfileResponseDTO> getCompanyProfile(@PathVariable Long companyId) {
        try {
            CompanyProfileResponseDTO profile = companyService.getCompanyProfile(companyId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint: GET /api/v1/companies/dashboard
     * Busca todos os produtos associados à empresa autenticada.
     * Alimenta a lista da CompanyDashboardScreen.jsx.
     * Requer autenticação (Token JWT).
     */
    @GetMapping("/dashboard")
    public ResponseEntity<List<ProductDetailResponseDTO>> getMyProducts(Authentication authentication) {

        // 🚨 O Spring Security deve bloquear isso, mas é um bom "fail-safe"
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Delega para o serviço, que irá extrair o ID da empresa do objeto
            // Authentication
            List<ProductDetailResponseDTO> products = productService.findProductsForLoggedInCompany(authentication);

            return ResponseEntity.ok(products);

        } catch (AccessDeniedException e) {
            // Captura se o usuário logado não for uma 'EMPRESA' (exceção lançada no
            // ProductService)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            // Captura outros erros (ex: empresa não encontrada para o usuário logado)
            return ResponseEntity.notFound().build();
        }
    }
}