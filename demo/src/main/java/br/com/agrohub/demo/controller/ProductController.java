package br.com.agrohub.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.AddProductRequestDTO;
import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.services.ProductService;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Novo Import

/**
 * Controller responsável pela exposição de produtos.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint: POST /api/v1/products
     * Rota para uma empresa adicionar um novo produto. Requer ROLE_EMPRESA.
     */
    @PostMapping
    public ResponseEntity<Void> addProduct(
            @Valid @RequestBody AddProductRequestDTO requestDTO,
            Authentication authentication) {
        try {
            productService.addProduct(requestDTO, authentication);
            return ResponseEntity.status(201).build(); // 201 Created
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            // Se o usuário ou empresa não for encontrado (geralmente 404)
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            e.printStackTrace();
            // 500 para qualquer outro erro interno não esperado
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint: GET /api/v1/products
     */
    @GetMapping
    public ResponseEntity<List<ProductCardResponseDTO>> getAllActiveProducts() {
        List<ProductCardResponseDTO> products = productService.findAllActiveProductsCard();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint: GET /api/v1/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCardResponseDTO> getProductById(@PathVariable Long id) {
        // Assume que EntityNotFoundException será lançada pelo Service se não encontrar
        ProductCardResponseDTO product = productService.findProductCardById(id);
        return ResponseEntity.ok(product);
    }
}