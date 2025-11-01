// CompanyController.java (Versão Final Corrigida)

package br.com.agrohub.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy; // Import necessário
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ProductDetailResponseDTO;
import br.com.agrohub.demo.services.CompanyService;
import br.com.agrohub.demo.services.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final ProductService productService;

    @Autowired
    public CompanyController(@Lazy CompanyService companyService, ProductService productService) {
        this.companyService = companyService;
        this.productService = productService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCompany(@Valid @RequestBody CompanyRegisterRequestDTO requestDTO) {
        companyService.registerCompany(requestDTO);
        return ResponseEntity.status(201).build(); // 201 CREATED
    }

    @GetMapping("/{companyId}/profile")
    public ResponseEntity<CompanyProfileResponseDTO> getCompanyProfile(@PathVariable Long companyId) {
        try {
            CompanyProfileResponseDTO profile = companyService.getCompanyProfile(companyId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<ProductDetailResponseDTO>> getMyProducts(Authentication authentication) {

        try {
            List<ProductDetailResponseDTO> products = productService.findProductsForLoggedInCompany(authentication);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }
}