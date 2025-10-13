package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Discount;

// Repositório que gerencia o acesso à tabela 'descontos'.
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Método customizado para buscar descontos ativos (útil para o catálogo)
    List<Discount> findByIsActiveTrue();

    // Método customizado para buscar descontos por um código específico (Ex: "VERAO20")
    List<Discount> findByCode(String code);

    // Método customizado para buscar descontos que se aplicam a um produto específico
    List<Discount> findByProductId(Long productId);
}
