package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Category;

// Repositório que gerencia o acesso à tabela 'categorias'.
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Método customizado que pode ser útil para o catálogo:
    // Buscar uma categoria pelo nome (usado para garantir que não haja categorias duplicadas, por exemplo)
    Optional<Category> findByCategoriaIgnoreCase(String categoria);
}
