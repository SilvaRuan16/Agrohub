package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ComentarioDTO implements Serializable {

    private Long id;
    private Long idCliente; 

    // Dados para exibição na tela (ClientProductDetailScreen.jsx)
    private String nomeCliente; 
    private Integer avaliacao; // Nota de 1 a 5 (Integer)
    private String comentario;
    private LocalDateTime dataComentario;

    // Construtor padrão
    public ComentarioDTO() {
    }

    // [1. Construtor Original de 6 campos]
    public ComentarioDTO(Long id, Long idCliente, String nomeCliente, Integer avaliacao, String comentario,
            LocalDateTime dataComentario) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataComentario = dataComentario;
    }

    // [2. CONSTRUTOR CORRIGIDO PARA O ProductMapper - 5 campos]
    // O ProductMapper envia (Long, String, String, Double, LocalDateTime)
    public ComentarioDTO(Long id, String nomeCliente, String comentario, Double rating,
            LocalDateTime dataComentario) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.comentario = comentario;
        
        // Converte o Double (rating) do Model para o Integer (avaliacao) do DTO
        this.avaliacao = rating != null ? rating.intValue() : 0;
        
        this.dataComentario = dataComentario;
        this.idCliente = null; // Definindo como null, pois o Mapper não o fornece
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }
    // ... (restante dos Getters/Setters) ...
    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(LocalDateTime dataComentario) {
        this.dataComentario = dataComentario;
    }
}