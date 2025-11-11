// Conteúdo de ClientServiceTest.java com as correções
package br.com.agrohub.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.agrohub.demo.dto.ClientProfileResponseDTO;
import br.com.agrohub.demo.dto.ClientRegisterRequestDTO;
import br.com.agrohub.demo.dto.ContactDTO;
import br.com.agrohub.demo.dto.EnderecoDTO;
import br.com.agrohub.demo.mappers.ClientMapper;
import br.com.agrohub.demo.models.Client;
import br.com.agrohub.demo.models.Contact;
import br.com.agrohub.demo.models.User;
import br.com.agrohub.demo.models.UserType;
import br.com.agrohub.demo.repository.ClientRepository;
import br.com.agrohub.demo.repository.ContactRepository;
import br.com.agrohub.demo.security.AuthSecurity;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private AuthSecurity authSecurity;

    @Captor
    private ArgumentCaptor<Client> clientCaptor;

    private ClientRegisterRequestDTO registerRequestDTO;
    private ContactDTO contactDTO;
    private EnderecoDTO enderecoDTO;
    private Contact mockContact;
    private User mockUser;
    @Mock
    private Client mockClient;

    @BeforeEach
    void setUp() {
        // --- 1. DTO de Requisição ---
        contactDTO = new ContactDTO();
        contactDTO.setEmail("novo.cliente@teste.com.br");
        contactDTO.setTelefone("11987654321");

        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("01000-000");

        registerRequestDTO = new ClientRegisterRequestDTO();
        registerRequestDTO.setSenha("senha123");
        registerRequestDTO.setCpf("12345678901");
        registerRequestDTO.setNomeCompleto("Novo Cliente Teste");
        registerRequestDTO.setCnpj(""); // Simula CNPJ vazio/nulo (Pessoa Física)
        registerRequestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        registerRequestDTO.setContact(contactDTO);
        registerRequestDTO.setEndereco(enderecoDTO);

        // --- 2. Entidades Mockadas ---
        mockContact = new Contact();
        mockContact.setId(10L);
        mockContact.setEmail("novo.cliente@teste.com.br");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("novo.cliente@teste.com.br");
        mockUser.setTipoUsuario(UserType.CLIENTE);
    }

    // =========================================================================
    // TESTES DE REGISTRO (registerClient)
    // =========================================================================

    /**
     * Teste para o registro bem-sucedido de um Cliente Pessoa Física.
     */
    @Test
    void testRegisterClient_Success_PF() {
        // 1. Instância REAL de Client para permitir mutação de estado pelo Service
        Client realClient = new Client();
        realClient.setCnpj(registerRequestDTO.getCnpj()); // CNPJ inicial é ""
        realClient.setCpf(registerRequestDTO.getCpf()); // CPF é "12345678901"
        realClient.setNomeCompleto(registerRequestDTO.getNomeCompleto());
        // Fim da configuração REAL
        when(clientMapper.toContact(registerRequestDTO.getContact())).thenReturn(mockContact);
        when(contactRepository.save(mockContact)).thenReturn(mockContact);

        when(authSecurity.registerNewUser(
                registerRequestDTO.getContact().getEmail(),
                registerRequestDTO.getCpf(),
                registerRequestDTO.getSenha(),
                UserType.CLIENTE))
                .thenReturn(mockUser);

        when(clientMapper.toClient(registerRequestDTO)).thenReturn(realClient);
        when(clientRepository.save(any(Client.class))).thenReturn(realClient);

        // ACT: Execução do método
        clientService.registerClient(registerRequestDTO);

        // ASSERT: Verificações de fluxo
        verify(contactRepository, times(1)).save(mockContact);
        verify(authSecurity, times(1)).registerNewUser(any(), any(), any(), any());

        verify(clientRepository, times(1)).save(clientCaptor.capture());
        Client savedClient = clientCaptor.getValue(); // savedClient é a instância REAL

        // Verifica que o CNPJ foi corrigido para null (lógica PF do serviço)
        assertThat(savedClient.getCnpj()).isNull(); // Agora deve ser null (passa)
    }

    // =========================================================================
    // TESTES DE PERFIL (getClientProfile)
    // =========================================================================

    /**
     * Teste para o cenário de sucesso na busca de perfil.
     */
    @Test
    void testGetClientProfile_Success() {
        // ARRANGE: Configuração dos Mocks
        Long userId = 1L;
        ClientProfileResponseDTO expectedDTO = new ClientProfileResponseDTO();
        expectedDTO.setId(userId);

        // 1. Simula a busca do Cliente pelo ID do Usuário
        when(clientRepository.findByUserId(userId)).thenReturn(Optional.of(mockClient));

        // 2. Simplifica a stubbing do Mapper para evitar o erro de Matcher
        when(clientMapper.toClientProfileDTO(
                any(Client.class), any(), any())) // Simplifica o uso de matchers
                .thenReturn(expectedDTO);

        // ACT: Execução do método
        ClientProfileResponseDTO result = clientService.getClientProfile(userId);

        // ASSERT: Verificações
        verify(clientRepository, times(1)).findByUserId(userId);

        // A verificação é mantida usando 'any()'
        verify(clientMapper, times(1)).toClientProfileDTO(
                any(Client.class),
                any(),
                any());

        assertThat(result).isEqualTo(expectedDTO);
    }

    /**
     * Teste para o cenário onde o Cliente não é encontrado para um ID de Usuário.
     * Deve lançar EntityNotFoundException.
     */
    @Test
    void testGetClientProfile_NotFound() {
        // ARRANGE: Configuração dos Mocks para retornar vazio
        Long userId = 99L;
        when(clientRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // ACT & ASSERT: Execução do método e verificação da exceção
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientProfile(userId);
        });

        // Verifica a mensagem da exceção
        assertThat(thrown.getMessage()).isEqualTo("Cliente não encontrado para o ID de usuário fornecido.");

        // Verifica se o Mapper NUNCA foi chamado
        verify(clientMapper, never()).toClientProfileDTO(any(), any(), any());
    }
}