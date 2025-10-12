package br.com.agrohub.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.agrohub.demo.dto.UsuarioDTO;
import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.Usuarios;
import br.com.agrohub.demo.repository.UsuariosRepository;

@ExtendWith(MockitoExtension.class)
class UsuariosServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private UsuariosRepository usuariosRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuariosService usuariosService;

    @Captor
    ArgumentCaptor<Usuarios> modelCaptor;

    private Usuarios sampleModel;

    private UsuarioDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleModel = new Usuarios();
        sampleModel.setId_usuario(1L);
        sampleModel.setEmail("jose");
        sampleModel.setSenha("$2a$hashed");

        sampleDto = new UsuarioDTO();
        sampleDto.setId_usuario(1L);
        sampleDto.setEmail("jose");
        sampleDto.setSenha("plain");
    }

    @Test
    void testAutenticarUsuarioComSucesso() {
        when(usuariosRepository.findByEmail("jose")).thenReturn(sampleModel);
        when(passwordEncoder.matches("plain", sampleModel.getSenha())).thenReturn(true);

        boolean ok = usuariosService.autenticar("jose", "plain");
        assertTrue(ok);

        verify(usuariosRepository).findByEmail("jose");
        verify(passwordEncoder).matches("plain", sampleModel.getSenha());
    }

    @Test
    void testTentarAutenticarLoginNull() {
        when(usuariosRepository.findByEmail("unknown")).thenReturn(null);
        assertFalse(usuariosService.autenticar("unknown", "qwe"));

        Usuarios uNoPass = new Usuarios();
        uNoPass.setEmail("nopass");
        uNoPass.setSenha(null);
        when(usuariosRepository.findByEmail("nopass")).thenReturn(uNoPass);
        assertFalse(usuariosService.autenticar("nopass", "any"));
    }

    // getTodos: sucesso (verifica que password é nulificado no DTO retornado)
    @Test
    void testGetTodosComSucesso() throws Exception {
        UsuarioDTO mappedDto = new UsuarioDTO();
        mappedDto.setId_usuario(1L);
        mappedDto.setEmail("jose");
        mappedDto.setSenha("something"); // mapper retorna com senha, service deve nulificar

        when(usuariosRepository.findByEmail("jose")).thenReturn(sampleModel);
        when(mapper.map(sampleModel, UsuarioDTO.class)).thenReturn(mappedDto);

        UsuarioDTO result = usuariosService.buscarTodos(mappedDto);
        assertNotNull(result);
        assertNull(result.getSenha(), "A senha deve ser removida do DTO retornado");
    }

    // getTodos: argumento nulo -> Exception
    @Test
    void testGetTodosNulo() {
        Exception ex = assertThrows(Exception.class, () -> usuariosService.buscarTodos(null));
        assertEquals("Usuário não informado.", ex.getMessage());
    }

    // getTodos: usuario nao encontrado -> Exception
    @Test
    void testGetTodosNãoEncontrado() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("noone");
        when(usuariosRepository.findByEmail("noone")).thenReturn(null);

        Exception ex = assertThrows(Exception.class, () -> usuariosService.buscarTodos(dto));
        assertEquals("Cliente não encontrado.", ex.getMessage());
    }

    // buscarPorId: encontrado
    @Test
    void testBuscarPorIdValido() throws Exception {
        when(usuariosRepository.findById(1L)).thenReturn(Optional.of(sampleModel));
        UsuarioDTO mapped = new UsuarioDTO();
        mapped.setId_usuario(1L);
        mapped.setEmail("jose");
        when(mapper.map(sampleModel, UsuarioDTO.class)).thenReturn(mapped);

        UsuarioDTO result = usuariosService.buscarPorId(1L);
        assertNotNull(result);
        assertEquals("jose", result.getEmail());
    }

    // buscarPorId: não encontrado
    @Test
    void testBuscarPorIdInvalido() {
        when(usuariosRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> usuariosService.buscarPorId(99L));
        assertEquals("ID inválido.", ex.getMessage());
    }

    // validarLogin: existe -> UsuarioDuplicadoException
    @Test
    void testTentarValidarLoginDuplicado() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("exists");
        when(usuariosRepository.existsByEmail("exists")).thenReturn(true);

        assertThrows(UsuarioDuplicadoException.class, () -> usuariosService.validarLogin(dto));
    }

    // cadastrar: sucesso -> senha codificada e salvo
    @Test
    void testCadastrarUsuarioComSucesso() throws Exception {
        UsuarioDTO dtoIn = new UsuarioDTO();
        dtoIn.setId_usuario(5L); // service zera id internamente
        dtoIn.setEmail("novo");
        dtoIn.setSenha("plainpass");

        Usuarios toSaveModel = new Usuarios();
        toSaveModel.setEmail("novo");
        // mapper.map(dto, LoginModel.class) deve retornar o entity que repository
        // receberá
        when(mapper.map(any(UsuarioDTO.class), eq(Usuarios.class))).thenReturn(toSaveModel);

        Usuarios savedModel = new Usuarios();
        savedModel.setId_usuario(10L);
        savedModel.setEmail("novo");
        savedModel.setSenha("$2a$hashed");
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(savedModel);

        when(usuariosRepository.existsByEmail("novo")).thenReturn(false);
        when(passwordEncoder.encode("plainpass")).thenReturn("$2a$hashed");
        UsuarioDTO mappedDto = new UsuarioDTO();
        mappedDto.setId_usuario(10L);
        mappedDto.setEmail("novo");
        // mapper.map(entity, LoginDTO.class) chamado ao final no toDTO:
        when(mapper.map(savedModel, UsuarioDTO.class)).thenReturn(mappedDto);

        UsuarioDTO result = usuariosService.cadastrar(dtoIn);
        assertNotNull(result);
        assertEquals(10L, result.getId_usuario());

        // verificamos que o DTO passado para mapper -> entidade teve a senha
        // codificada:
        ArgumentCaptor<UsuarioDTO> dtoCaptor = ArgumentCaptor.forClass(UsuarioDTO.class);
        verify(mapper).map(dtoCaptor.capture(), eq(UsuarioDTO.class));
        UsuarioDTO dtoPassedToMap = dtoCaptor.getValue();
        assertNotNull(dtoPassedToMap.getSenha());
        assertEquals("$2a$hashed", dtoPassedToMap.getSenha());
    }

    // cadastrar: sem senha -> Exception
    @Test
    void testTentarCadastrarUsuarioSemSenha() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("newUser");
        dto.setSenha(null);

        Exception ex = assertThrows(Exception.class, () -> usuariosService.cadastrar(dto));
        assertEquals("Senha não informada.", ex.getMessage());
    }

    // atualizar: sucesso -> verifica mapeamento e chamada ao repository
    @Test
    void testAtualizarComSucesso() throws Exception {
    // Arrange
    Long id_usuario = 3L;

    Usuarios existing = new Usuarios();
    existing.setId_usuario(id_usuario);
    existing.setEmail("pessoa");
    existing.setSenha("$2a$oldHash");

    UsuarioDTO newData = new UsuarioDTO();
    newData.setEmail("pessoa");
    newData.setSenha("newPlain");

    when(usuariosRepository.findById(id_usuario)).thenReturn(Optional.of(existing));
    when(usuariosRepository.existsByEmail("pessoa")).thenReturn(false);
    when(passwordEncoder.encode("newPlain")).thenReturn("$2a$newHash");

    Usuarios toSaveModel = new Usuarios();
    when(mapper.map(any(UsuarioDTO.class),
    eq(Usuarios.class))).thenReturn(toSaveModel);

    // repository.save devolve o entity salvo (com id e senha codificada)
    Usuarios savedModel = new Usuarios();
    savedModel.setId_usuario(id_usuario);
    savedModel.setEmail("pessoa");
    savedModel.setSenha("$2a$newHash");
    when(usuariosRepository.save(toSaveModel)).thenReturn(savedModel);

    // mapper: entity -> DTO (resultado final)
    UsuarioDTO mappedDto = new UsuarioDTO();
    mappedDto.setId_usuario(id_usuario);
    mappedDto.setEmail("pessoa");
    when(mapper.map(savedModel, UsuarioDTO.class)).thenReturn(mappedDto);

    // Act
    UsuarioDTO result = usuariosService.atualizar(id_usuario, newData);

    // Assert - resultado
    assertNotNull(result);
    assertEquals(id_usuario, result.getId_usuario());
    assertEquals("pessoa", result.getEmail());

    ArgumentCaptor<UsuarioDTO> dtoCaptor = ArgumentCaptor.forClass(UsuarioDTO.class);
    verify(mapper).map(dtoCaptor.capture(), eq(UsuarioDTO.class));
    UsuarioDTO dtoPassed = dtoCaptor.getValue();
    assertEquals(id_usuario, dtoPassed.getId_usuario(), "id deve ser setado antes do map/save");
    assertEquals("$2a$newHash", dtoPassed.getSenha(), "senha deve estar codificada antes do map/save");

    // Verificações de interação importantes
    verify(usuariosRepository).findById(id_usuario);
    verify(usuariosRepository).existsByEmail("pessoa");
    verify(usuariosRepository).save(toSaveModel);
    verify(passwordEncoder).encode("newPlain");
    verify(mapper).map(savedModel, UsuarioDTO.class);

    // garante que não houve interações extras inesperadas (opcional)
    verifyNoMoreInteractions(usuariosRepository, mapper, passwordEncoder);
    }

    // atualizar: quando senha nula -> manter hash atual
    @Test
    void testTentarAtualizarQuandoSenhaForNull() throws Exception {
    Long id_usuario = 2L;
    Usuarios existing = new Usuarios();
    existing.setId_usuario(id_usuario);
    existing.setEmail("pessoa");
    existing.setSenha("$2a$oldHash");

    UsuarioDTO newData = new UsuarioDTO();
    newData.setEmail("pessoa");
    newData.setSenha(null);

    when(usuariosRepository.findById(id_usuario)).thenReturn(Optional.of(existing));
    when(mapper.map(any(UsuarioDTO.class), eq(Usuarios.class)))
    .thenAnswer(invocation -> {
    UsuarioDTO dto = invocation.getArgument(0); // Captura o DTO que foi passado
    Usuarios model = new Usuarios();
    model.setEmail(dto.getEmail());
    model.setSenha(dto.getSenha());
    return model;
    });
    when(usuariosRepository.save(any(Usuarios.class))).thenReturn(existing);
    when(mapper.map(existing, UsuarioDTO.class)).thenReturn(newData);

    UsuarioDTO result = usuariosService.atualizar(id_usuario, newData);
    assertNotNull(result);

    // capturamos o DTO passado para mapper.map(dto->entity) para garantir que a
    // senha foi ajustada
    ArgumentCaptor<UsuarioDTO> dtoCaptor = ArgumentCaptor.forClass(UsuarioDTO.class);
    verify(mapper).map(dtoCaptor.capture(), eq(UsuarioDTO.class));
    UsuarioDTO dtoPassed = dtoCaptor.getValue();
    assertEquals("$2a$oldHash", dtoPassed.getSenha(), "Senha deve ter sido mantida (hash existente)");
    }

    @Test
    void testdeletarUsuario() throws Exception {
        Long id_usuario = 55L;
        String res = usuariosService.deletar(id_usuario);
        assertEquals("Excluído", res);
        verify(usuariosRepository).deleteById(id_usuario);
    }
}
