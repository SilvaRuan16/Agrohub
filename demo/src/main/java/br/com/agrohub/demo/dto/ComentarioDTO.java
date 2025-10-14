package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data; // Adicionado por conveniência, mas opcional
import lombok.NoArgsConstructor;

/**
 * DTO para exibir informações de comentários e avaliação de produtos.
 */
@Data // Gera Getters, Setters, toString, hashCode, equals
@NoArgsConstructor
// @AllArgsConstructor removido porque a ordem dos campos não é o construtor desejado pelo Mapper
@Builder // Útil para criar objetos de forma flexível (opcional)
public class ComentarioDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long idCliente; // Mantido para o construtor de 6 campos, se necessário

    // Dados para exibição na tela
    private String nomeCliente; 
    private Integer avaliacao; // Nota de 1 a 5 (Integer)
    private String comentario;
    private LocalDateTime dataComentario;

    // Construtor que o ProductMapper está tentando usar
    // O ProductMapper envia: (Long, String, String, Integer, LocalDateTime)
    public ComentarioDTO(Long id, String nomeCliente, String comentario, Integer avaliacao,
            LocalDateTime dataComentario) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.comentario = comentario;
        this.avaliacao = avaliacao; // O tipo agora é consistente (Integer)
        this.dataComentario = dataComentario;
        this.idCliente = null; // Inicializa o campo não fornecido no mapper
    }

    // Se você ainda precisar do construtor de 6 campos:
    public ComentarioDTO(Long id, Long idCliente, String nomeCliente, Integer avaliacao, String comentario,
            LocalDateTime dataComentario) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataComentario = dataComentario;
    }
}