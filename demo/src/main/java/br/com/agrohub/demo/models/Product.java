package br.com.agrohub.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    // Relacionamento Many-to-One: Produto pertence a uma Empresa (Produtor)
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @Column(name = "preco_compra", precision = 10, scale = 2)
    private BigDecimal precoCompra;

    @Column(name = "preco_min_venda", precision = 10, scale = 2)
    private BigDecimal precoMinVenda;

    @Column(name = "margem_lucro", precision = 5, scale = 2)
    private BigDecimal margemLucro;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque;

    @Column(name = "quantidade_min_estoque")
    private Integer quantidadeMinEstoque;

    @Column(columnDefinition = "text")
    private String descricao;
    
    // Relacionamentos One-to-One/Many-to-One
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category category; // Usando Category.java (a ser criado)

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id")
    private PaymentMethod paymentMethod; // Usando PaymentMethod.java (a ser criado)

    @OneToOne
    @JoinColumn(name = "desconto_id")
    private Discount discount; // Usando Discount.java (a ser criado)

    @OneToOne
    @JoinColumn(name = "informacao_adicional_id")
    private AdditionalInfo additionalInfo; // Usando AdditionalInfo.java (a ser criado)
    
    // Relacionamento One-to-Many com Imagens e Comentários
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images; // Usando Image.java (a ser criado)

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // Usando Comment.java (a ser criado)
}