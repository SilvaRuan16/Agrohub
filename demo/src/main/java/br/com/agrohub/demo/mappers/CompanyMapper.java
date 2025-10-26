package br.com.agrohub.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ContactDTO; // ✅ CORREÇÃO 1: Importa o DTO de Contato
import br.com.agrohub.demo.dto.HistoricoVendaDTO;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Contact; // ✅ CORREÇÃO 1: Importa a Entidade Contact
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;

/**
 * Mapper responsável pela conversão entre a Entidade Company e seus DTOs de
 * Registro e Perfil.
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

    // NOTA: Este método toUserEntity não é usado no Service final, mas está
    // ajustado.
    public User toUserEntity(CompanyRegisterRequestDTO dto) {
        User user = new User();
        // ✅ CORREÇÃO: Email deve vir do Contact
        user.setEmail(dto.getContact().getEmail());
        user.setCnpj(dto.getCnpj());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setTipoUsuario(UserType.EMPRESA);
        return user;
    }

    /**
     * ✅ CORREÇÃO 2: Adiciona o mapeamento para Contact.
     * Necessário para o método registerCompany no CompanyService.
     */
    public Contact toContact(ContactDTO dto) {
        if (dto == null)
            return null;

        Contact contact = new Contact();
        contact.setEmail(dto.getEmail());
        contact.setTelefone(dto.getTelefone()); // Assumindo que ContatoDTO tem getTelefone()
        // Mapear outros campos de contato aqui, se houver.
        return contact;
    }

    // ✅ toCompanyEntity está correto e recebe os dois argumentos, alinhado com o
    // Service.
    public Company toCompanyEntity(CompanyRegisterRequestDTO dto, User user) {
        Company company = new Company();
        company.setRazaoSocial(dto.getRazaoSocial());
        company.setNomeFantasia(dto.getNomeFantasia());
        company.setCnpj(dto.getCnpj());
        company.setDataFundacao(dto.getDataFundacao());

        // company.setContact(commonMapper.createContactEntity(dto.getTelefone(),
        // dto.getEmail(), null)); // Linha original comentada

        company.setUser(user);
        return company;
    }

    // =================================================================
    // 2. MAPEAR PARA DTOs (Perfil - Entity to Response)
    // =================================================================

    /**
     * CORREÇÃO: Altera a assinatura para List<Pedido> (a entidade real de
     * transação).
     */
    public CompanyProfileResponseDTO toCompanyProfileResponseDTO(
            User user,
            Company company,
            List<Pedido> pedidos, // <<< Tipo corrigido aqui.
            List<Address> addresses) {

        if (company == null)
            return null;

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
        /*
         * dto.setEnderecos(addresses.stream()
         * .map(commonMapper::toAddressDTO)
         * .collect(Collectors.toList()));
         */

        // 3. HISTÓRICO DE VENDAS
        dto.setHistoricoVendas(this.toHistoricoVendaDTOList(pedidos));

        return dto;
    }

    /**
     * Mapeamento auxiliar do Histórico de Vendas.
     */
    private List<HistoricoVendaDTO> toHistoricoVendaDTOList(List<Pedido> pedidos) {
        if (pedidos == null)
            return List.of();

        // Mapeamento de Pedido para HistoricoVendaDTO, usando as informações do Pedido
        return pedidos.stream().map(pedido -> new HistoricoVendaDTO(
                pedido.getId(),
                "Venda #" + pedido.getId(),
                // Assumindo que Pedido.java tem getItens() e que getItens().size() é a
                // quantidade de itens vendidos
                pedido.getItens() != null ? pedido.getItens().size() : 0,
                pedido.getValorTotal(),
                pedido.getDataPedido(),
                pedido.getStatus().name() // Assumindo que Pedido tem o método getStatus()
        )).collect(Collectors.toList());
    }
}