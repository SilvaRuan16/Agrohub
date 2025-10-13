package br.com.agrohub.demo.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "representantes_empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Representative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_representante")
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(unique = true, nullable = false, length = 15)
    private String rg;

    // Relacionamento Many-to-One: Representante tem 1 Cargo
    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Role role; // Usando Role.java (a ser criado)

    // Relacionamento One-to-One: Representante tem 1 Contato
    @OneToOne
    @JoinColumn(name = "contato_id", nullable = false)
    private Contact contact; 
    
    // Relacionamento Many-to-Many com Company (lado inverso)
    @ManyToMany(mappedBy = "representatives", fetch = FetchType.LAZY)
    private List<Company> companies;
}