package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.PaymentMethod;

// Repositório que gerencia o acesso à tabela 'metodos_pagamento'.
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    // Método customizado para buscar métodos de pagamento pelo nome (ex: "Cartão de Crédito")
    List<PaymentMethod> findByFormaPagamento(String formaPagamento);

    // Método customizado para buscar apenas métodos que estão ativos
    List<PaymentMethod> findByIsActiveTrue();
}
