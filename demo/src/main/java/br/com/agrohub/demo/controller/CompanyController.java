package br.com.agrohub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.services.CompanyService;
import jakarta.validation.Valid;

// OBS: Semelhante ao cliente, o ID seria extraído do token JWT.

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCompany(@Valid @RequestBody CompanyRegisterRequestDTO requestDTO) {
        companyService.registerCompany(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
}