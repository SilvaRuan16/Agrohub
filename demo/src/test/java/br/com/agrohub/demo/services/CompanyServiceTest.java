package br.com.agrohub.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient; // Adicionado para resolver UnnecessaryStubbingException
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.agrohub.demo.dto.CompanyProfileResponseDTO;
import br.com.agrohub.demo.dto.CompanyRegisterRequestDTO;
import br.com.agrohub.demo.dto.ContactDTO;
import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.exceptions.ValidationException;
import br.com.agrohub.demo.mappers.CompanyMapper;
import br.com.agrohub.demo.models.Address;
import br.com.agrohub.demo.models.Company;
import br.com.agrohub.demo.models.Contact;
import br.com.agrohub.demo.models.Pedido;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.CompanyRepository;
import br.com.agrohub.demo.repository.ContactRepository;
import br.com.agrohub.demo.repository.PedidoRepository;
import br.com.agrohub.demo.security.AuthSecurity;

/**
 * Testes unitários para o CompanyService.
 * Foco em mockar dependências e verificar a lógica de negócio interna.
 */
@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    // Mocks para as dependências
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CompanyMapper companyMapper;
    @Mock
    private AuthSecurity authSecurity;

    // Dados simulados
    private CompanyRegisterRequestDTO registerRequestDTO;
    private ContactDTO contactDTO;
    private EnderecoDTO enderecoDTO;
    private Contact mockContact;
    private User mockUser;
    @Mock
    private Company mockCompany;
    private final Long companyId = 1L;
    private final String rawCnpj = "12.345.678/0001-90";
    private final String cleanCnpj = "12345678000190";

    // Tipo List<Address> para corresponder à assinatura do Mapper
    private List<Address> mockAddresses;

    @BeforeEach
    void setUp() {
        // --- 1. DTO de Requisição ---
        contactDTO = new ContactDTO();
        contactDTO.setEmail("empresa@teste.com.br");
        contactDTO.setTelefone("11987654321");
        contactDTO.setWebsite("www.agrohub.com.br");

        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("01000-000");

        registerRequestDTO = new CompanyRegisterRequestDTO();
        registerRequestDTO.setEmail("empresa@teste.com.br");
        registerRequestDTO.setSenha("securepass");
        registerRequestDTO.setCnpj(rawCnpj); // Com máscara
        registerRequestDTO.setRazaoSocial("AgroHub Teste S.A.");
        registerRequestDTO.setNomeFantasia("AgroHub");
        registerRequestDTO.setDataFundacao(LocalDate.of(2020, 1, 1));
        registerRequestDTO.setContact(contactDTO);
        registerRequestDTO.setEndereco(enderecoDTO);

        // --- 2. Entidades Mockadas ---
        mockContact = new Contact();
        mockContact.setId(10L);
        mockContact.setEmail("empresa@teste.com.br");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("empresa@teste.com.br");
        mockUser.setTipoUsuario(UserType.EMPRESA);
        mockUser.setCnpj(cleanCnpj);

        // 3. Inicialização da Lista de Endereços
        mockAddresses = List.of();
    }

    // =========================================================================
    // TESTES DE REGISTRO (registerCompany)
    // =========================================================================

    /**
     * Teste para o registro bem-sucedido de uma Empresa.
     */
    @Test
    void testRegisterCompany_Success() {
        // ARRANGE: Configuração dos Mocks
        // 1. Validações (não existem)
        when(companyRepository.existsByCnpj(cleanCnpj)).thenReturn(false);
        when(authSecurity.userExists(contactDTO.getEmail())).thenReturn(false);

        // 2. Mapear e Salvar Contato
        when(companyMapper.toContact(registerRequestDTO.getContact())).thenReturn(mockContact);
        when(contactRepository.save(mockContact)).thenReturn(mockContact);

        // 3. Registrar Novo Usuário (AuthSecurity)
        when(authSecurity.registerNewUser(
                contactDTO.getEmail(),
                cleanCnpj, // Deve usar o CNPJ LIMPO
                registerRequestDTO.getSenha(),
                UserType.EMPRESA))
                .thenReturn(mockUser);

        // 4. Mapear Empresa
        when(companyMapper.toCompanyEntity(eq(registerRequestDTO), eq(mockUser))).thenReturn(mockCompany);

        // 5. Salvar Empresa
        when(companyRepository.save(any(Company.class))).thenReturn(mockCompany);

        // ACT: Execução do método
        companyService.registerCompany(registerRequestDTO);

        // ASSERT: Verificações de fluxo
        // 1. Deve checar se o CNPJ e o email já existem
        verify(companyRepository, times(1)).existsByCnpj(cleanCnpj);
        verify(authSecurity, times(1)).userExists(contactDTO.getEmail());

        // 2. Deve salvar o Contato
        verify(contactRepository, times(1)).save(mockContact);

        // 3. Deve registrar o novo Usuário via AuthSecurity (com CNPJ limpo)
        verify(authSecurity, times(1)).registerNewUser(
                eq("empresa@teste.com.br"),
                eq(cleanCnpj),
                eq("securepass"),
                eq(UserType.EMPRESA));

        // 4. Deve mapear, configurar e salvar a Empresa
        verify(companyMapper, times(1)).toCompanyEntity(eq(registerRequestDTO), eq(mockUser));
        verify(mockCompany, times(1)).setUser(mockUser);
        verify(mockCompany, times(1)).setContact(mockContact);
        verify(mockCompany, times(1)).setCnpj(cleanCnpj);
        verify(companyRepository, times(1)).save(mockCompany);
    }

    @Test
    void testRegisterCompany_Failure_CnpjAlreadyExists() {
        // ARRANGE: Simula que o CNPJ já existe
        when(companyRepository.existsByCnpj(cleanCnpj)).thenReturn(true);

        // ACT & ASSERT
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            companyService.registerCompany(registerRequestDTO);
        });

        // Verifica a mensagem da exceção
        assertThat(thrown.getMessage()).isEqualTo("Empresa com CNPJ " + cleanCnpj + " já registrada.");

        // Garante que nenhum save foi tentado
        verify(contactRepository, never()).save(any());
        verify(authSecurity, never()).registerNewUser(any(), any(), any(), any());
        verify(companyRepository, never()).save(any());
    }

    // =========================================================================
    // TESTES DE PERFIL (getCompanyProfile)
    // =========================================================================

    /**
     * Teste para o cenário de sucesso na busca de perfil de empresa.
     */
    @Test
    void testGetCompanyProfile_Success() {
        // ARRANGE: Configuração dos Mocks
        List<Pedido> mockPedidos = Collections.emptyList(); // Mock de dados de histórico

        CompanyProfileResponseDTO expectedDTO = new CompanyProfileResponseDTO();
        expectedDTO.setId(companyId);

        // 1. Simula a busca da Empresa pelo ID
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));

        // 2. Simula a busca dos dados relacionados
        when(pedidoRepository.findAll()).thenReturn(mockPedidos);

        // Simula os getters necessários
        when(mockCompany.getUser()).thenReturn(mockUser);

        // CORREÇÃO: Usamos lenient() para suprimir o UnnecessaryStubbingException.
        // O método getCompanyAddresses() é stubs, mas pode não ser chamado se o
        // mapeamento for preguiçoso (LAZY) ou se o Service o ignorar.
        lenient().when(mockCompany.getCompanyAddresses()).thenReturn(List.of());

        // 3. Simula o mapeamento
        when(companyMapper.toCompanyProfileResponseDTO(
                eq(mockUser),
                eq(mockCompany),
                eq(mockPedidos),
                eq(mockAddresses))) // <--- Usa List<Address> (o que o Mapper espera)
                .thenReturn(expectedDTO);

        // ACT: Execução do método
        CompanyProfileResponseDTO result = companyService.getCompanyProfile(companyId);

        // ASSERT: Verificações
        // 1. Deve buscar a empresa pelo ID
        verify(companyRepository, times(1)).findById(companyId);

        // 2. Deve buscar os pedidos
        verify(pedidoRepository, times(1)).findAll();

        // 3. Deve chamar o Mapper corretamente (com List<Address>)
        verify(companyMapper, times(1)).toCompanyProfileResponseDTO(
                eq(mockUser),
                eq(mockCompany),
                eq(mockPedidos),
                eq(mockAddresses));

        // 4. O resultado deve ser o DTO esperado
        assertThat(result).isEqualTo(expectedDTO);
    }

    /**
     * Teste para o cenário onde a Empresa não é encontrada.
     * Deve lançar RuntimeException (conforme a implementação atual do
     * CompanyService).
     */
    @Test
    void testGetCompanyProfile_NotFound() {
        // ARRANGE: Configuração dos Mocks para retornar vazio
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // ACT & ASSERT: Execução do método e verificação da exceção
        // O CompanyService.java atualmente lança RuntimeException
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            companyService.getCompanyProfile(companyId);
        });

        // Verifica a mensagem da exceção
        assertThat(thrown.getMessage()).isEqualTo("Empresa não encontrada com o ID: " + companyId);

        // Verifica se o Mapper e Repositório de Pedidos NUNCA foram chamados
        verify(companyMapper, never()).toCompanyProfileResponseDTO(any(), any(), any(), any());
        verify(pedidoRepository, never()).findAll();
    }
}