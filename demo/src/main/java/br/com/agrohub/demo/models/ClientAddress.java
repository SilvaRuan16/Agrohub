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
@Table(name = "enderecos_clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco_cliente")
    private Long id;

    // Relacionamento Many-to-One: Muitos endereços de cliente para um Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Client client;

    // Relacionamento Many-to-One: Muitos endereços de cliente para um Endereço
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id")
    private Address address;

    @Column(name = "tipo_endereco", length = 50)
    private String tipoEndereco; // Ex: "COBRANÇA", "ENTREGA"
}