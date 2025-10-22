package br.com.agrohub.demo.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    // Relacionamento One-to-One com User (Chave Estrangeira)
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "nome_completo", nullable = false, length = 120)
    private String nomeCompleto;

    @Column(unique = true, length = 11)
    private String cpf;

    @Column(unique = true, length = 14)
    private String cnpj; // Se for cliente PJ

    @Column(unique = true, length = 15)
    private String rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;
    
    // ⭐ CAMPOS ADICIONADOS PARA COMPATIBILIDADE COM DTO/MAPPER ⭐
    @Column(name = "rede_social", length = 100)
    private String redeSocial; 

    @Column(name = "website", length = 100)
    private String website;
    // -------------------------------------------------------------

    // Relacionamento One-to-One com Contato
    @OneToOne
    @JoinColumn(name = "contato_id", nullable = false)
    private Contact contact;

    // Relacionamento One-to-Many com Endereços
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientAddress> enderecos; // Lombok gera getEnderecos()

    // Relacionamento One-to-Many com Pedidos
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos; // Lombok gera getPedidos()
}