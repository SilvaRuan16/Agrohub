package br.com.agrohub.demo.controller;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO; // Adicionado
import br.com.agrohub.demo.services.CompanyService;
import br.com.agrohub.demo.services.ProductService; // Adicionado
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Adicionado
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException; // Adicionado
import java.util.List; // Adicionado

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final ProductService productService; // Adicionado

    @Autowired
    public CompanyController(CompanyService companyService, ProductService productService) { // Modificado
        this.companyService = companyService;
        this.productService = productService; // Adicionado
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
     */
    @GetMapping("/dashboard")
    public ResponseEntity<List<ProductDetailResponseDTO>> getMyProducts(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // A lógica de "descobrir quem está logado" foi movida para o ProductService
            List<ProductDetailResponseDTO> products = productService.findProductsForLoggedInCompany(authentication);
            return ResponseEntity.ok(products);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Caso o usuário não seja uma empresa
        } catch (RuntimeException e) {
            // Isso captura o UsernameNotFoundException ou outros erros
            return ResponseEntity.notFound().build();
        }
    }
}