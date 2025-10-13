package br.com.agrohub.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long id;

    // Relacionamento One-to-One com User (Chave Estrangeira)
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Column(unique = true, nullable = false, length = 14)
    private String cnpj;

    @Column(name = "data_fundacao")
    private LocalDate dataFundacao;

    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;

    // Relacionamento One-to-One com Contato (Contato Principal)
    @OneToOne
    @JoinColumn(name = "contato_principal_id")
    private Contact contact;

    // Relacionamento One-to-Many com Endereços
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyAddress> companyAddresses; // Usando CompanyAddress.java (a ser criado)

    // Relacionamento One-to-Many com Produtos
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
    
    // Relacionamento Many-to-Many com Representantes
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "empresas_representantes",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "representante_id")
    )
    private List<Representative> representatives; // Usando Representative.java (a ser criado)

}