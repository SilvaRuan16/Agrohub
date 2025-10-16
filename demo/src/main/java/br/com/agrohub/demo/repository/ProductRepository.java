package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Product;

// Repositório que gerencia o acesso à tabela 'produtos'.
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Busca produtos por nome (para a barra de pesquisa na tela h1.png)
    // O 'ContainingIgnoreCase' permite buscar parcialmente e sem diferenciar
    // maiúsculas/minúsculas.
    List<Product> findByNomeContainingIgnoreCase(String nome);

    // 2. Busca produtos por ID de Categoria (para o filtro de categorias em h1.png)
    List<Product> findByCategoryId(Long categoryId);

    // 3. Busca produtos por ID de Empresa (para o dashboard do produtor)
    List<Product> findByCompanyId(Long companyId);

    // 4. Busca produtos que estão ativos (para o catálogo público)
    List<Product> findByActiveTrue();

    // 5. Busca produtos que correspondem a um tipo específico (ex: Orgânico,
    // Artesanal)
    // DENTRO DE ProductRepository.java
    List<Product> findByAdditionalInfoProductTypeId(Long productTypeId);
}
