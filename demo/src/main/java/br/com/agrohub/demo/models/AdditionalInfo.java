package br.com.agrohub.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "informacoes_adicionais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informacao")
    private Long id;

    private String produtor;

    private String municipio;

    @Column(name = "cnpj_produtor", length = 14)
    private String cnpjProdutor;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Address address; 

    @ManyToOne
    @JoinColumn(name = "contato_id")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "tipo_produto_id")
    private ProductType productType; // Usando ProductType.java (a ser criado)
}