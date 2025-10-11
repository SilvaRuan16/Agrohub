package br.com.agrohub.demo.models;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "usuarios")
public class Usuarios implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id_usuario;

   @Column(name = "email")
   private String email;

   @Column(name = "senha")
   private String senha;

   @Column(name = "tipo_usuario")
   private String tipo_usuario;

   @Column(name = "data_criacao")
   private LocalDate data_criacao;
}
