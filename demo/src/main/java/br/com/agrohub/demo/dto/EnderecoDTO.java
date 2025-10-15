package br.com.agrohub.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;          // Gera Getters, Setters, toString, equals, hashCode
import lombok.Data; // Gera o construtor padrão (sem argumentos)
import lombok.NoArgsConstructor; // Gera o construtor com todos os campos

/**
 * DTO para representar dados de endereço.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO implements Serializable {
    
    private Long id; // ID do endereço (Adicionado)
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento; // Opcional (apto, bloco, etc.)
    private String logradouro;
}