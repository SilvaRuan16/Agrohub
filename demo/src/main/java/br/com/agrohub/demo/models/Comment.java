package br.com.agrohub.demo.models;

import java.time.LocalDateTime;

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
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    // Relacionamento Many-to-One: Comentário pertence a um Produto
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Product product;

    // Relacionamento Many-to-One: Comentário pode ser de um Cliente (Pode ser null: "on delete set null")
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;

    @Column(nullable = false, columnDefinition = "text")
    private String comentario;

    // Avaliação de 1 a 5
    private Integer avaliacao;

    @Column(name = "data_comentario")
    private LocalDateTime dataComentario;
}