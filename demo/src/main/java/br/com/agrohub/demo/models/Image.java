package br.com.agrohub.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "imagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagem")
    private Long id;

    // Relacionamento Many-to-One: Imagem pertence a um Produto
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Product product;

    // 🎯 CORREÇÃO APLICADA AQUI: Renomeado de 'imagem' para 'url'
    @Column(nullable = false, length = 255)
    private String url; // Agora o Lombok gera o método getUrl()
}