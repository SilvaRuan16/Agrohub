package br.com.agrohub.demo.mappers;

import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.models.Address;
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
        
        // CORREÇÃO: Usando setters do Address.java em Português
        entity.setRua(dto.getRua()); 
        entity.setNumero(dto.getNumero());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        
        // AVISO: Os campos 'Complemento' e 'Referencia' não existem na sua entidade Address.java
        // Se a entidade Address for atualizada, estas linhas devem ser descomentadas:
        // entity.setComplemento(dto.getComplemento()); 
        // entity.setReferencia(dto.getReferencia());
        
        return entity;
    }

    /**
     * Converte a entidade Address para EnderecoDTO.
     */
    public EnderecoDTO toAddressDTO(Address entity) {
        if (entity == null) return null;

        EnderecoDTO dto = new EnderecoDTO();
        dto.setCep(entity.getCep());
        
        // CORREÇÃO: Usando getters do Address.java em Português
        dto.setRua(entity.getRua());
        dto.setNumero(entity.getNumero());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setEstado(entity.getEstado());
        
        // AVISO: Se os campos 'Complemento' e 'Referencia' forem adicionados ao Address.java,
        // estas linhas devem ser atualizadas para buscar os dados corretamente.
        
        return dto;
    }

    // ===============================
    // Mapeamento: Contact (Contato)
    // ===============================

    /**
     * Cria uma entidade Contact a partir de dados de um DTO de registro.
     * Assinatura corrigida para 3 argumentos (conforme ClientMapper e CompanyMapper)
     */
    public Contact createContactEntity(String telefone, String email, String urlSite) { 
        Contact entity = new Contact();
        
        // CORREÇÃO: Usando setters do Contact.java (assumindo que usa os campos em Português que você forneceu: telefone, email, urlSite)
        entity.setTelefone(telefone); 
        entity.setEmail(email);
        entity.setUrlSite(urlSite); 
        
        // Se a assinatura for diferente ou o Contact.java usar outros nomes (ex: redeSocial), precisa ser ajustado.
        
        return entity;
    }
}