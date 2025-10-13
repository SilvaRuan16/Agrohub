package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime; // Para registrar a data e hora do comentário

public class ComentarioDTO implements Serializable {

    private Long id;
    private Long idCliente; // Opcional: Se for anônimo, pode vir null ou ser usado para rastreio

    // Dados para exibição na tela (ClientProductDetailScreen.jsx)
    private String nomeCliente; // Nome do cliente que avaliou
    private Integer avaliacao; // Nota de 1 a 5
    private String comentario;
    private LocalDateTime dataComentario;

    // Construtor padrão
    public ComentarioDTO() {
    }

    // Construtor com todos os campos
    public ComentarioDTO(Long id, Long idCliente, String nomeCliente, Integer avaliacao, String comentario,
            LocalDateTime dataComentario) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataComentario = dataComentario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

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