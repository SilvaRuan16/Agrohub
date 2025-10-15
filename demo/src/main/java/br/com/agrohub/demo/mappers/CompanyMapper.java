package br.com.agrohub.demo.mappers;

// ... (Imports de DTOs e Models continuam os mesmos) ...
import java.util.List;
import java.util.stream.Collectors; 

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.HistoricoVendaDTO;
import br.com.agrohub.demo.models.Address; // Enum UserType importado aqui
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.HistoricoVenda;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;

/**
 * Mapper responsável pela conversão entre a Entidade Company e seus DTOs de Registro e Perfil.
 */
@Component
public class CompanyMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    public CompanyMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // 1. MAPEAR PARA ENTIDADES (Registro - Request to Entity)
    // =================================================================

    public User toUserEntity(CompanyRegisterRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCnpj(dto.getCnpj()); 
        user.setSenha(passwordEncoder.encode(dto.getSenha())); 
        
        // CORREÇÃO: Usando o enum externo UserType.EMPRESA
        user.setTipoUsuario(UserType.EMPRESA); 
        
        return user;
    }

    public Company toCompanyEntity(CompanyRegisterRequestDTO dto, User user) {
        Company company = new Company();
        company.setRazaoSocial(dto.getRazaoSocial());
        company.setNomeFantasia(dto.getNomeFantasia());
        company.setCnpj(dto.getCnpj());
        company.setDataFundacao(dto.getDataFundacao()); 
        
        // CORREÇÃO: Usando a ordem de argumentos corrigida (telefone, email, urlSite)
        company.setContact(commonMapper.createContactEntity(dto.getTelefone(), dto.getEmail(), null));
        
        company.setUser(user); 
        return company;
    }


    // =================================================================
    // 2. MAPEAR PARA DTOs (Perfil - Entity to Response)
    // =================================================================

    public CompanyProfileResponseDTO toCompanyProfileResponseDTO(User user, Company company, List<HistoricoVenda> historicoVendas, List<Address> addresses) {
        if (company == null) return null;

        CompanyProfileResponseDTO dto = new CompanyProfileResponseDTO();
        
        // 1. DADOS DE USUÁRIO E EMPRESA
        dto.setId(company.getId());
        dto.setEmail(user.getEmail());
        dto.setRazaoSocial(company.getRazaoSocial());
        dto.setNomeFantasia(company.getNomeFantasia());
        dto.setCnpj(user.getCnpj()); 
        dto.setDataFundacao(company.getDataFundacao());
        dto.setTelefone(company.getContact() != null ? company.getContact().getTelefone() : null);

        // 2. ENDEREÇOS CADASTRADOS
        dto.setEnderecos(addresses.stream()
            .map(commonMapper::toAddressDTO)
            .collect(Collectors.toList()));

        // 3. HISTÓRICO DE VENDAS
        dto.setHistoricoVendas(this.toHistoricoVendaDTOList(historicoVendas));

        return dto;
    }
    
    /**
     * Mapeamento auxiliar do Histórico de Vendas
     */
    private List<HistoricoVendaDTO> toHistoricoVendaDTOList(List<HistoricoVenda> historicoVendas) {
        if (historicoVendas == null) return List.of();
        
        return historicoVendas.stream().map(venda -> new HistoricoVendaDTO(
            venda.getId(),
            venda.getNomeProduto(), 
            venda.getQuantidade(), 
            venda.getValorTotal(),
            venda.getDataVenda(),
            venda.getStatusVenda() 
        )).collect(Collectors.toList()); 
    }
}