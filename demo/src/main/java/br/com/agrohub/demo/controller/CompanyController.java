package br.com.agrohub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.services.CompanyService;

// OBS: Semelhante ao cliente, o ID seria extraído do token JWT.

@RestController
@RequestMapping("/api/v1/companies") 
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Endpoint para buscar o perfil completo da empresa (dados, endereços, histórico de vendas).
     * URL: GET /api/v1/companies/{companyId}/profile
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
}