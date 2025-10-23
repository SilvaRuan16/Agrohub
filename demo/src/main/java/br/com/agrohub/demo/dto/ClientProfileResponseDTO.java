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
    
    // ⭐ CORREÇÃO: ADICIONADO CNPJ (Estava faltando e causava erro no Mapper)
    private String cnpj; 
    
    private LocalDate dataNascimento;
    
    // 2. DADOS DE CONTATO
    private String telefone; // Contato principal
    
    // ⭐ ADICIONADOS: Para exibir todos os dados da Entidade Contact
    private String redeSocial; 
    private String website; // Corresponde a urlSite na Entidade Contact
    
    // 3. ENDEREÇOS CADASTRADOS
    private List<EnderecoDTO> enderecos;

    // 4. HISTÓRICO DE COMPRAS
    private List<HistoricoPedidoDTO> historicoPedidos;
}