package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    // O JpaRepository já fornece todos os métodos de CRUD (save, findById, findAll, etc.).
    // Podemos adicionar métodos de busca customizados aqui, se necessário.
    
    // Exemplo: Buscar tipo de produto pelo nome
    // Optional<ProductType> findByTipo(String tipo);
}
