package br.com.agrohub.demo.models;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "descontos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_desconto")
    private Long id;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal percentual;
    
    // 🎯 CORREÇÃO APLICADA AQUI: Campo para indicar se o desconto está ativo
    @Column(name = "ativo", nullable = false)
    private boolean ativo; // O Lombok agora gera o método public boolean isAtivo()

    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @OneToMany(mappedBy = "discount")
    private List<Product> products;

}