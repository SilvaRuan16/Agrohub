package br.com.agrohub.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO com as informações mínimas da empresa (vendedor) para serem exibidas
 * nos cards e detalhes de produto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor // Necessário para o construtor usado no ProductMapper
public class CompanyResumeDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nomeFantasia; // Usado para exibição rápida ao cliente
}