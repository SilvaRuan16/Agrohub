/*package br.com.agrohub.demo.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.agrohub.demo.dto.LoginDTO;
import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.LoginModel;
import br.com.agrohub.demo.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Captor
    ArgumentCaptor<LoginModel> modelCaptor;

    private LoginModel sampleModel;
    private LoginDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleModel = new LoginModel();
        sampleModel.setId(1L);
        sampleModel.setUsername("jose");
        sampleModel.setPassword("$2a$hashed"); // exemplo de hash

        sampleDto = new LoginDTO();
        sampleDto.setId(1L);
        sampleDto.setUsername("jose");
        sampleDto.setPassword("plain");
    }
    /*
    @Test
    void testAutenticarUsuarioComSucesso() {
        when(usuarioRepository.findByUsername("jose")).thenReturn(sampleModel);
        when(passwordEncoder.matches("plain", sampleModel.getPassword())).thenReturn(true);

        boolean ok = loginService.autenticar("jose", "plain");
        assertTrue(ok);

        verify(usuarioRepository).findByUsername("jose");
        verify(passwordEncoder).matches("plain", sampleModel.getPassword());
    }

    @Test
    void testTentarAutenticarLoginNull() {
        when(usuarioRepository.findByUsername("unknown")).thenReturn(null);
        assertFalse(loginService.autenticar("unknown", "qwe"));

        LoginModel uNoPass = new LoginModel();
        uNoPass.setUsername("nopass");
        uNoPass.setPassword(null);
        when(usuarioRepository.findByUsername("nopass")).thenReturn(uNoPass);
        assertFalse(loginService.autenticar("nopass", "any"));
    }
    
    // getTodos: sucesso (verifica que password é nulificado no DTO retornado)
    @Test
    void testGetTodosComSucesso() throws Exception {
        LoginDTO mappedDto = new LoginDTO();
        mappedDto.setId(1L);
        mappedDto.setUsername("jose");
        mappedDto.setPassword("something"); // mapper retorna com senha, service deve nulificar

        when(usuarioRepository.findByUsername("jose")).thenReturn(sampleModel);
        when(mapper.map(sampleModel, LoginDTO.class)).thenReturn(mappedDto);

        LoginDTO result = loginService.getTodos(mappedDto);
        assertNotNull(result);
        assertNull(result.getPassword(), "A senha deve ser removida do DTO retornado");
    }

    // getTodos: argumento nulo -> Exception
    @Test
    void testGetTodosNulo() {
        Exception ex = assertThrows(Exception.class, () -> loginService.getTodos(null));
        assertEquals("Usuário não informado.", ex.getMessage());
    }

    // getTodos: usuario nao encontrado -> Exception
    @Test
    void testGetTodosNãoEncontrado() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("noone");
        when(usuarioRepository.findByUsername("noone")).thenReturn(null);

        Exception ex = assertThrows(Exception.class, () -> loginService.getTodos(dto));
        assertEquals("Cliente não encontrado.", ex.getMessage());
    }

    // buscarPorId: encontrado
    @Test
    void testBuscarPorIdValido() throws Exception {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(sampleModel));
        LoginDTO mapped = new LoginDTO();
        mapped.setId(1L);
        mapped.setUsername("jose");
        when(mapper.map(sampleModel, LoginDTO.class)).thenReturn(mapped);

        LoginDTO result = loginService.buscarPorId(1L);
        assertNotNull(result);
        assertEquals("jose", result.getUsername());
    }

    // buscarPorId: não encontrado
    @Test
    void testBuscarPorIdInvalido() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> loginService.buscarPorId(99L));
        assertEquals("ID inválido.", ex.getMessage());
    }

    // validarLogin: existe -> UsuarioDuplicadoException
    @Test
    void testTentarValidarLoginDuplicado() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("exists");
        when(usuarioRepository.existsByUsername("exists")).thenReturn(true);

        assertThrows(UsuarioDuplicadoException.class, () -> loginService.validarLogin(dto));
    }

    // cadastrar: sucesso -> senha codificada e salvo
    @Test
    void testCadastrarUsuarioComSucesso() throws Exception {
        LoginDTO dtoIn = new LoginDTO();
        dtoIn.setId(5L); // service zera id internamente
        dtoIn.setUsername("novo");
        dtoIn.setPassword("plainpass");

        LoginModel toSaveModel = new LoginModel();
        toSaveModel.setUsername("novo");
        // mapper.map(dto, LoginModel.class) deve retornar o entity que repository
        // receberá
        when(mapper.map(any(LoginDTO.class), eq(LoginModel.class))).thenReturn(toSaveModel);

        LoginModel savedModel = new LoginModel();
        savedModel.setId(10L);
        savedModel.setUsername("novo");
        savedModel.setPassword("$2a$hashed");
        when(usuarioRepository.save(any(LoginModel.class))).thenReturn(savedModel);

        when(usuarioRepository.existsByUsername("novo")).thenReturn(false);
        when(passwordEncoder.encode("plainpass")).thenReturn("$2a$hashed");
        LoginDTO mappedDto = new LoginDTO();
        mappedDto.setId(10L);
        mappedDto.setUsername("novo");
        // mapper.map(entity, LoginDTO.class) chamado ao final no toDTO:
        when(mapper.map(savedModel, LoginDTO.class)).thenReturn(mappedDto);

        LoginDTO result = loginService.cadastrar(dtoIn);
        assertNotNull(result);
        assertEquals(10L, result.getId());

        // verificamos que o DTO passado para mapper -> entidade teve a senha
        // codificada:
        ArgumentCaptor<LoginDTO> dtoCaptor = ArgumentCaptor.forClass(LoginDTO.class);
        verify(mapper).map(dtoCaptor.capture(), eq(LoginModel.class));
        LoginDTO dtoPassedToMap = dtoCaptor.getValue();
        assertNotNull(dtoPassedToMap.getPassword());
        assertEquals("$2a$hashed", dtoPassedToMap.getPassword());
    }

    // cadastrar: sem senha -> Exception
    @Test
    void testTentarCadastrarUsuarioSemSenha() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("newUser");
        dto.setPassword(null);

        Exception ex = assertThrows(Exception.class, () -> loginService.cadastrar(dto));
        assertEquals("Senha não informada.", ex.getMessage());
    }

    // atualizar: sucesso -> verifica mapeamento e chamada ao repository
    // @Test
    // void testAtualizarComSucesso() throws Exception {
    // // Arrange
    // Long id = 3L;

    // LoginModel existing = new LoginModel();
    // existing.setId(id);
    // existing.setUsername("pessoa");
    // existing.setPassword("$2a$oldHash");

    // LoginDTO newData = new LoginDTO();
    // newData.setUsername("pessoa");
    // newData.setPassword("newPlain"); // senha nova -> deve ser codificada

    // when(usuarioRepository.findById(id)).thenReturn(Optional.of(existing));
    // when(usuarioRepository.existsByUsername("pessoa")).thenReturn(false);
    // when(passwordEncoder.encode("newPlain")).thenReturn("$2a$newHash");

    // // mapper: quando converter DTO->Entity, retornamos um objeto "toSaveModel"
    // (não
    // // importa o conteúdo)
    // LoginModel toSaveModel = new LoginModel();
    // when(mapper.map(any(LoginDTO.class),
    // eq(LoginModel.class))).thenReturn(toSaveModel);

    // // repository.save devolve o entity salvo (com id e senha codificada)
    // LoginModel savedModel = new LoginModel();
    // savedModel.setId(id);
    // savedModel.setUsername("pessoa");
    // savedModel.setPassword("$2a$newHash");
    // when(usuarioRepository.save(toSaveModel)).thenReturn(savedModel);

    // // mapper: entity -> DTO (resultado final)
    // LoginDTO mappedDto = new LoginDTO();
    // mappedDto.setId(id);
    // mappedDto.setUsername("pessoa");
    // when(mapper.map(savedModel, LoginDTO.class)).thenReturn(mappedDto);

    // // Act
    // LoginDTO result = loginService.atualizar(id, newData);

    // // Assert - resultado
    // assertNotNull(result);
    // assertEquals(id, result.getId());
    // assertEquals("pessoa", result.getUsername());

    // // Assert - verificamos que o DTO passado para map(dto->entity) teve id e
    // senha
    // // adequados
    // ArgumentCaptor<LoginDTO> dtoCaptor = ArgumentCaptor.forClass(LoginDTO.class);
    // verify(mapper).map(dtoCaptor.capture(), eq(LoginModel.class));
    // LoginDTO dtoPassed = dtoCaptor.getValue();
    // assertEquals(id, dtoPassed.getId(), "id deve ser setado antes do map/save");
    // assertEquals("$2a$newHash", dtoPassed.getPassword(), "senha deve estar
    // codificada antes do map/save");

    // // Verificações de interação importantes
    // verify(usuarioRepository).findById(id);
    // verify(usuarioRepository).existsByUsername("pessoa");
    // verify(usuarioRepository).save(toSaveModel);
    // verify(passwordEncoder).encode("newPlain");
    // verify(mapper).map(savedModel, LoginDTO.class);

    // // garante que não houve interações extras inesperadas (opcional)
    // verifyNoMoreInteractions(usuarioRepository, mapper, passwordEncoder);
    // }

    // // atualizar: quando senha nula -> manter hash atual
    // @Test
    // void testTentarAtualizarQuandoSenhaForNull() throws Exception {
    // Long id = 2L;
    // LoginModel existing = new LoginModel();
    // existing.setId(id);
    // existing.setUsername("pessoa");
    // existing.setPassword("$2a$oldHash");

    // LoginDTO newData = new LoginDTO();
    // newData.setUsername("pessoa");
    // newData.setPassword(null);

    // when(usuarioRepository.findById(id)).thenReturn(Optional.of(existing));
    // when(mapper.map(any(LoginDTO.class), eq(LoginModel.class)))
    // .thenAnswer(invocation -> {
    // LoginDTO dto = invocation.getArgument(0); // Captura o DTO que foi passado
    // LoginModel model = new LoginModel();
    // model.setUsername(dto.getUsername());
    // model.setPassword(dto.getPassword());
    // return model;
    // });
    // when(usuarioRepository.save(any(LoginModel.class))).thenReturn(existing);
    // when(mapper.map(existing, LoginDTO.class)).thenReturn(newData);

    // LoginDTO result = loginService.atualizar(id, newData);
    // assertNotNull(result);

    // // capturamos o DTO passado para mapper.map(dto->entity) para garantir que a
    // // senha foi ajustada
    // ArgumentCaptor<LoginDTO> dtoCaptor = ArgumentCaptor.forClass(LoginDTO.class);
    // verify(mapper).map(dtoCaptor.capture(), eq(LoginModel.class));
    // LoginDTO dtoPassed = dtoCaptor.getValue();
    // assertEquals("$2a$oldHash", dtoPassed.getPassword(), "Senha deve ter sido
    // mantida (hash existente)");
    // }

    @Test
    void testdeletarUsuario() throws Exception {
        Long id = 55L;
        // não precisa stubbing para deleteById
        String res = loginService.deletar(id);
        assertEquals("Excluído", res);
        verify(usuarioRepository).deleteById(id);
    }
}
*/