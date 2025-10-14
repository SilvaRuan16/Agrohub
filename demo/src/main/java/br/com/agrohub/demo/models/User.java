package br.com.agrohub.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column; 
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    // O campo 'senha' é o que define o Setter como setSenha() (corrigido nos Mappers)
    private String senha; 

    @Enumerated(EnumType.STRING) // Garante que salve a string 'EMPRESA' ou 'CLIENTE' no banco
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private UserType tipoUsuario; // Usa o Enum importado

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // REMOVIDO: A definição interna do enum UserType foi removida para usar o arquivo UserType.java criado.
    /*
    public enum UserType {
        EMPRESA,
        CLIENTE
    }
    */

    // Relacionamentos One-to-One
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Client client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Company company;
}