package br.com.agrohub.demo.services;

import java.util.List;

import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.exceptions.ValidationException;
import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.mappers.CompanyMapper;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Contact;
// OBS: Assumindo que você tem repositórios para Company e Pedidos (Histórico de Vendas)
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.repository.CompanyRepository;
import br.com.agrohub.demo.repository.ContactRepository;
import br.com.agrohub.demo.repository.PedidoRepository;
// OBS: Assumindo que você tem um repositório para Address (ou CompanyAddress)
// Para o CompanyMapper que criamos, ele espera List<Address>
import br.com.agrohub.demo.security.AuthSecurity;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PedidoRepository pedidoRepository; // Para buscar o histórico de vendas
    private final CompanyMapper companyMapper;
    private final AuthSecurity authSecurity;
    private final ContactRepository contactRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, PedidoRepository pedidoRepository,
            CompanyMapper companyMapper, AuthSecurity authSecurity, ContactRepository contactRepository) {
        this.companyRepository = companyRepository;
        this.pedidoRepository = pedidoRepository;
        this.companyMapper = companyMapper;
        this.authSecurity = authSecurity;
        this.contactRepository = contactRepository;
    }

    /**
     * Registra uma nova empresa, limpando o CNPJ para salvar apenas dígitos.
     */
    @Transactional
    public void registerCompany(CompanyRegisterRequestDTO requestDTO) { //

        // 1. Pré-validação de CNPJ e E-mail
        
        // O frontend (LoginScreen) envia o CNPJ sem formatação (só dígitos).
        // Devemos garantir que o CNPJ seja salvo da mesma forma no registro.
        
        if (requestDTO.getCnpj() == null) { //
            throw new ValidationException("O CNPJ é obrigatório.");
        }
        
        // Remove todos os caracteres não numéricos (pontos, barras, traços)
        String cnpj = requestDTO.getCnpj().replaceAll("\\D", ""); //
        
        String email = requestDTO.getContact().getEmail(); //

        if (!StringUtils.hasText(cnpj)) {
            throw new ValidationException("O CNPJ é obrigatório.");
        }
        
        // Agora ele verifica o CNPJ limpo (só dígitos)
        if (companyRepository.existsByCnpj(cnpj)) {
            throw new ValidationException("Empresa com CNPJ " + cnpj + " já registrada.");
        }
        if (authSecurity.userExists(email)) { //
            throw new ValidationException("Usuário com e-mail " + email + " já existe.");
        }

        // 2. Mapeamento e Persistência de Contato (usando o Mapper)
        Contact contact = companyMapper.toContact(requestDTO.getContact()); //
        contact = contactRepository.save(contact); //

        // 3. Registro do Novo Usuário no Módulo de Segurança (passando o CNPJ limpo)
        User newUser = authSecurity.registerNewUser(
                email,
                cnpj, // Passa o CNPJ limpo (só dígitos)
                requestDTO.getSenha(), //
                UserType.EMPRESA); //

        // 4. Mapeamento e Configuração da Empresa
        Company company = companyMapper.toCompanyEntity(requestDTO, newUser); //
        company.setUser(newUser); //
        company.setContact(contact); //

        // 5. Ajuste do CNPJ (garantindo que o CNPJ limpo seja salvo)
        // Garante que a entidade Company também salve o CNPJ limpo
        company.setCnpj(cnpj); 

        // 6. Persistência da Empresa
        companyRepository.save(company); //
    }

    /**
     * Busca o perfil completo de uma empresa, incluindo seus dados, endereços e
     * histórico de vendas.
     * * @param companyId O ID da empresa.
     * @return O DTO de resposta com todos os dados.
     * @throws RuntimeException se a empresa não for encontrada.
     */
    public CompanyProfileResponseDTO getCompanyProfile(Long companyId) { //

        // 1. Buscar a Entidade Principal (Company)
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + companyId));

        // 2. Buscar Entidades Relacionadas: Histórico de Vendas (Pedidos)
        // (Lógica original mantida)
        List<Pedido> historicoVendas = pedidoRepository.findAll(); //

        // 3. Buscar Entidades Relacionadas: Endereços
        // (Lógica original mantida)
        List<Address> addresses = List.of(); //

        // 4. Mapear para o DTO de Perfil
        return companyMapper.toCompanyProfileResponseDTO(
                company.getUser(),
                company,
                historicoVendas,
                addresses
        );
    }
}