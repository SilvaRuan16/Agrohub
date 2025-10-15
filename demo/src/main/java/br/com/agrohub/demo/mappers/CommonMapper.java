package br.com.agrohub.demo.mappers;

import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.models.ClientAddress; 
import br.com.agrohub.demo.models.Contact;

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
    public Address toAddressEntity(EnderecoDTO dto) {
        if (dto == null) return null;

        Address entity = new Address();
        entity.setCep(dto.getCep());
        entity.setRua(dto.getRua()); 
        entity.setNumero(dto.getNumero());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        entity.setComplemento(dto.getComplemento()); 
        
        return entity;
    }

    /**
     * 1. MÉTODO PARA CLIENTE: Converte a entidade ClientAddress (tabela de ligação) para EnderecoDTO.
     * Usado pelo ClientMapper.
     */
    public EnderecoDTO toAddressDTO(ClientAddress entity) {
        if (entity == null) return null;

        Address actualAddress = entity.getAddress(); 
        if (actualAddress == null) return null;

        EnderecoDTO dto = new EnderecoDTO();
        
        dto.setRua(actualAddress.getRua()); 
        dto.setCep(actualAddress.getCep());
        dto.setNumero(actualAddress.getNumero());
        dto.setBairro(actualAddress.getBairro());
        dto.setCidade(actualAddress.getCidade());
        dto.setEstado(actualAddress.getEstado());
        dto.setComplemento(actualAddress.getComplemento()); 
        dto.setId(entity.getId()); // ID da tabela de ligação
        
        return dto;
    }
    
    /**
     * 2. MÉTODO PARA EMPRESA: Converte a entidade Address (genérica) para EnderecoDTO.
     * Usado pelo CompanyMapper.
     */
    public EnderecoDTO toAddressDTO(Address entity) {
        if (entity == null) return null;

        EnderecoDTO dto = new EnderecoDTO();
        
        dto.setRua(entity.getRua()); 
        dto.setCep(entity.getCep());
        dto.setNumero(entity.getNumero());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setEstado(entity.getEstado());
        dto.setComplemento(entity.getComplemento()); 
        dto.setId(entity.getId()); // ID da tabela Address
        
        return dto;
    }

    // ===============================
    // Mapeamento: Contact (Contato)
    // ===============================

    /**
     * Cria uma entidade Contact a partir de dados de um DTO de registro.
     */
    public Contact createContactEntity(String telefone, String email, String urlSite) { 
        Contact entity = new Contact();
        
        entity.setTelefone(telefone); 
        entity.setEmail(email);
        entity.setUrlSite(urlSite); 
        
        return entity;
    }
}