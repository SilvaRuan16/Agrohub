package br.com.agrohub.demo.mappers;

import br.com.agrohub.demo.dto.EnderecoDTO; // DTOs Comuns
import br.com.agrohub.demo.models.Contact;        // CORRIGIDO: models.Contact
import br.com.agrohub.demo.models.Address;       // CORRIGIDO: models.Address
import org.springframework.stereotype.Component;

/**
 * Mapper para entidades e DTOs de uso comum em diversas entidades (Address, Contact).
 */
@Component
public class CommonMapper {

    // ===============================
    // Mapeamento: Address (Endereço)
    // ===============================

    /**
     * Converte EnderecoDTO para a entidade Address.
     */
    public Address toAddressEntity(EnderecoDTO dto) { // Renomeado para toAddressEntity
        if (dto == null) return null;

        Address entity = new Address();
        entity.setCep(dto.getCep());
        entity.setStreet(dto.getRua()); // Assumindo que Rua na DTO mapeia para Street na Entidade
        entity.setNumber(dto.getNumero());
        entity.setNeighborhood(dto.getBairro());
        entity.setCity(dto.getCidade());
        entity.setState(dto.getEstado());
        entity.setComplement(dto.getComplemento());
        entity.setReference(dto.getReferencia());
        return entity;
    }

    /**
     * Converte a entidade Address para EnderecoDTO.
     */
    public EnderecoDTO toAddressDTO(Address entity) { // Renomeado para toAddressDTO
        if (entity == null) return null;

        EnderecoDTO dto = new EnderecoDTO();
        dto.setCep(entity.getCep());
        dto.setRua(entity.getStreet());
        dto.setNumero(entity.getNumber());
        dto.setBairro(entity.getNeighborhood());
        dto.setCidade(entity.getCity());
        dto.setEstado(entity.getState());
        dto.setComplemento(entity.getComplement());
        dto.setReferencia(entity.getReference());
        return dto;
    }

    // ===============================
    // Mapeamento: Contact (Contato)
    // ===============================

    /**
     * Cria uma entidade Contact a partir de dados de um DTO de registro.
     */
    public Contact createContactEntity(String telefone, String urlSite, String email) { // Renomeado para createContactEntity
        Contact entity = new Contact();
        entity.setPhone(telefone); // Assumindo que Telefone mapeia para Phone
        entity.setWebsiteUrl(urlSite); 
        entity.setEmail(email);
        return entity;
    }
}