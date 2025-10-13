package br.com.agrohub.demo.mappers;

import br.com.agrohub.demo.dto.user.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.user.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.user.HistoricoVendaDTO;
import br.com.agrohub.demo.models.Company;    // CORRIGIDO: models no plural
import br.com.agrohub.demo.models.User;       // CORRIGIDO: models no plural
import br.com.agrohub.demo.models.Pedido;     // CORRIGIDO: models no plural
import br.com.agrohub.demo.models.Address;    // Usando Address, conforme AdditionalInfo
import br.com.agrohub.demo.models.UserType;  // Assumindo que UserType está em models
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre a Entidade Company e seus DTOs de Registro e Perfil.
 * Utiliza CommonMapper para Endereco (Address) e Contato (Contact).
 */
@Component
public class CompanyMapper {

    private final CommonMapper commonMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanyMapper(CommonMapper commonMapper, PasswordEncoder passwordEncoder) {
        this.commonMapper = commonMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // =================================================================
    // MAPEAR PARA ENTIDADES (Registro - Request to Entity)
    // =================================================================

    /**
     * Cria a entidade User a partir do DTO de Registro.
     */
    public User toUserEntity(CompanyRegisterRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCnpj(dto.getCnpj());
        user.setPassword(passwordEncoder.encode(dto.getSenha()));
        user.setUserType(UserType.EMPRESA);
        return user;
    }

    /**
     * Cria a entidade Company a partir do DTO de Registro, associando-a ao User.
     */
    public Company toCompanyEntity(CompanyRegisterRequestDTO dto, User user) {
        Company company = new Company();
        company.setRazaoSocial(dto.getRazaoSocial());
        company.setNomeFantasia(dto.getNomeFantasia());
        company.setInscricaoEstadual(dto.getInscricaoEstadual());
        company.setUsuario(user); 
        
        // O commonMapper precisa ser ajustado para usar Contact (com 's') e Address (com 's')
        company.setContato(commonMapper.createContactEntity(dto.getTelefone(), dto.getUrlSite(), dto.getEmail()));
        
        return company;
    }


    // =================================================================
    // MAPEAR PARA DTOs (Perfil - Entity to Response)
    // =================================================================

    /**
     * Converte as entidades de Perfil da Empresa para o Response DTO.
     */
    public CompanyProfileResponseDTO toCompanyProfileResponseDTO(User user, Company company, List<Pedido> vendas, List<Address> addresses) {
        if (company == null) return null;

        CompanyProfileResponseDTO dto = new CompanyProfileResponseDTO();
        
        // 1. DADOS DE USUÁRIO E EMPRESA
        dto.setId(company.getId());
        dto.setEmail(user.getEmail());
        dto.setRazaoSocial(company.getRazaoSocial());
        dto.setNomeFantasia(company.getNomeFantasia());
        dto.setCnpj(user.getCnpj());
        dto.setInscricaoEstadual(company.getInscricaoEstadual());
        dto.setTelefone(company.getContato() != null ? company.getContato().getTelefone() : null);
        dto.setUrlSite(company.getContato() != null ? company.getContato().getUrlSite() : null);

        // 2. ENDEREÇOS CADASTRADOS
        // O método toAddressDTO precisa ser ajustado no CommonMapper
        dto.setEnderecos(addresses.stream()
            .map(commonMapper::toAddressDTO)
            .collect(Collectors.toList()));

        // 3. HISTÓRICO DE VENDAS
        dto.setHistoricoVendas(this.toHistoricoVendaDTOList(vendas));

        return dto;
    }
    
    /**
     * Mapeamento auxiliar do Histórico de Vendas
     */
    private List<HistoricoVendaDTO> toHistoricoVendaDTOList(List<Pedido> vendas) {
        return vendas.stream().map(pedido -> new HistoricoVendaDTO(
            pedido.getId(),
            "Venda para #" + pedido.getCliente().getNomeCompleto(), 
            pedido.getItens().size(), 
            pedido.getValorTotal(),
            pedido.getDataPedido(),
            pedido.getStatus().name()
        )).collect(Collectors.toList());
    }
}