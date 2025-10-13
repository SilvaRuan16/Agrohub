package br.com.agrohub.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List; // <<< IMPORTAÇÃO NECESSÁRIA

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
    private User user; // Assume-se que User.java está no mesmo pacote ou foi importado

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

    // Relacionamento One-to-One com Contato
    @OneToOne
    @JoinColumn(name = "contato_id", nullable = false)
    private Contact contact; // Usando Contact.java (existe)

    // Relacionamento One-to-Many com Endereços
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    // ClientAddress deve ser criado no próximo passo
    private List<ClientAddress> clientAddresses; 
}