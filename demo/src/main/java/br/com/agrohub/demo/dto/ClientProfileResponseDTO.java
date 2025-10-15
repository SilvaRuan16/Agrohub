package br.com.agrohub.demo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Resposta para a tela de Perfil do Cliente.
 * @Data: Gera Getters, Setters, toString, hashCode e equals.
 * @NoArgsConstructor: Gera o construtor padrão.
 * @AllArgsConstructor: Gera o construtor com todos os campos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfileResponseDTO implements Serializable {
    
    // 1. DADOS DE USUÁRIO E CLIENTE
    private Long id; // ID do Cliente
    private String email;
    private String nomeCompleto;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String telefone; // Contato principal

    // 2. ENDEREÇOS CADASTRADOS
    private List<EnderecoDTO> enderecos;

    // 3. HISTÓRICO DE COMPRAS
    private List<HistoricoPedidoDTO> historicoPedidos;
}