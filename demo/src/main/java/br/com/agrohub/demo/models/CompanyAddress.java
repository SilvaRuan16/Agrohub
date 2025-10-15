package br.com.agrohub.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresas_enderecos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa_endereco")
    private Long id;

    // Relacionamento Many-to-One: Muitos endereços de empresa para uma Empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Company company;

    // Relacionamento Many-to-One: Muitos endereços de empresa para um Endereço
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id")
    private Address address;

    @Column(name = "tipo_endereco", length = 50)
    private String tipoEndereco; // Ex: "MATRIZ", "FILIAL"

    @Column(name = "principal", nullable = false)
    private boolean principal;
}