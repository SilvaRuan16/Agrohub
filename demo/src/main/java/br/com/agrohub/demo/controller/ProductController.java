package br.com.agrohub.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrohub.demo.dto.ProductCardResponseDTO;
import br.com.agrohub.demo.services.ProductService;

/**
 * Controller responsável pela exposição de produtos para o Cliente.
 * Roteia as requisições das telas ClientDashboardScreen.jsx e ClientProductDetailScreen.jsx.
 * OBS: Rotas para cadastro/edição ficam no CompanyController.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint: GET /api/v1/products
     * Busca todos os produtos ativos e retorna no formato Card.
     * Alimenta a lista principal do ClientDashboardScreen.jsx.
     */
    @GetMapping
    public ResponseEntity<List<ProductCardResponseDTO>> getAllActiveProducts() {
        // A lógica de busca e mapeamento está no Service
        List<ProductCardResponseDTO> products = productService.findAllActiveProductsCard();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint: GET /api/v1/products/{id}
     * Busca um único produto ativo pelo ID no formato Card.
     * Pode ser usado para a tela de detalhes ou ao clicar no card.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCardResponseDTO> getProductById(@PathVariable Long id) {
        // A lógica de busca, filtro de ativo e mapeamento está no Service
        ProductCardResponseDTO product = productService.findProductCardById(id);
        return ResponseEntity.ok(product);
    }

    // Você pode adicionar um endpoint de busca com filtros e paginação aqui:
    /*
    @GetMapping("/search")
    public ResponseEntity<List<ProductCardResponseDTO>> searchProducts(@RequestParam String query) {
        // ...
    }
    */
}