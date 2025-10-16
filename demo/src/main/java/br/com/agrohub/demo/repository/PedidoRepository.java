package br.com.agrohub.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca todos os pedidos feitos por um Cliente específico (Histórico de Compras).
     * Usado no ClientService.java.
     */
    List<Pedido> findByClientId(Long clientId);

    /**
     * Busca todos os pedidos onde os itens foram vendidos por uma determinada Empresa (Histórico de Vendas).
     * OBS: Isso exige que você tenha a relação correta entre Pedido/ItemPedido/Produto/Empresa
     * A maneira mais simples (e mais provável de funcionar no seu modelo de dados) é usar uma consulta JPQL.
     * Caso a JPQL falhe, você pode buscar por um campo simples no Pedido se tiver 'empresa_id' lá.
     * * **Sugestão simples (se for ManyToMany entre Pedido e Empresa) é por ItemPedido:**
     * List<Pedido> findByItensProductCompanyId(Long companyId);
     * * **Usaremos o findByItensProductCompanyId, que é o padrão do Spring para relacionamentos aninhados (ItemPedido.product.company.id):**
     * Usado no CompanyService.java.
     */
    List<Pedido> findByItensProductCompanyId(Long companyId);
}