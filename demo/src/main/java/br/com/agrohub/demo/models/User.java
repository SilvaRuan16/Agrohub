package br.com.agrohub.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data // Gera Getters, Setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(unique = true, length = 150)
    private String email;

    @Column(unique = true, length = 11)
    private String cpf; // Pode ser nulo se for CNPJ puro

    @Column(unique = true, length = 14)
    private String cnpj; // Pode ser nulo se for CPF puro

    @Column(nullable = false)
    private String senha; // O nome 'senha' é o que está no banco, mas pode ser 'password' no Java para ser mais idiomático

    @Enumerated(EnumType.STRING) // Garante que salve a string 'EMPRESA' ou 'CLIENTE' no banco
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private UserType tipoUsuario;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Enum para o tipo de usuário (melhora a segurança e leitura)
    public enum UserType {
        EMPRESA,
        CLIENTE
    }

    // Relacionamentos One-to-One (se o usuário for cliente OU empresa)
    // Opcional: Apenas para facilitar a navegação (fetch automático se necessário)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Client client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Company company;
}