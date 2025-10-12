package br.com.agrohub.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class ContatoDTO {
    private Long id_contato;
    private String telefone;
    private String email;
    private String rede_social;
    private String url_site;
}
