package br.com.agrohub.demo.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    
    // Campo de validação do banco 'check (percentual >= 0 and percentual <= 100)'
    // Essa lógica deve ser implementada na camada Service ou via validação JSR-303 (@Min, @Max).
}