package br.com.agrohub.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contatos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato")
    private Long id;

    private String telefone;

    private String email;

    @Column(name = "rede_social", columnDefinition = "text")
    private String redeSocial;

    @Column(name = "url_site", columnDefinition = "text")
    private String urlSite;

    @OneToOne(mappedBy = "contact")
    private Client client;

    @OneToOne(mappedBy = "contact") 
    private Company company;
}