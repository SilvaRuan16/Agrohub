package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}