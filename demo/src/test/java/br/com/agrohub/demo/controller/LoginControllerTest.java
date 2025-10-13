/*package br.com.agrohub.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agrohub.demo.dto.LoginDTO;
import br.com.agrohub.demo.service.LoginService;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetTodos() throws Exception {
        // arrange
        LoginDTO response = new LoginDTO();
        response.setUsername("user1");

        // garante que qualquer LoginDTO passado ao serviço retorna o objeto response
        given(loginService.getTodos(any(LoginDTO.class))).willReturn(response);

        // act + assert (adiciona .andDo(print()) para ver saída em caso de falha)
        mockMvc.perform(get("/login").param("username", "user1"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));

        // verify: capturamos o argumento que o controller passou para o service
        ArgumentCaptor<LoginDTO> captor = ArgumentCaptor.forClass(LoginDTO.class);
        verify(loginService).getTodos(captor.capture());

        // checamos o username do DTO enviado ao service pelo controller
        assertEquals("user1", captor.getValue().getUsername());
    }

    @Test
    public void testBuscarPorId() throws Exception {
        // arrange
        LoginDTO response = new LoginDTO();
        response.setUsername("buscarUser");
        given(loginService.buscarPorId(1L)).willReturn(response);

        // act + assert
        mockMvc.perform(get("/login/{id}", 1L)
                .with(user("agrohub")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("buscarUser"));

        verify(loginService).buscarPorId(1L);
    }

    @Test
    public void testBuscarPorIdDandoErro() throws Exception {
        // arrange
        given(loginService.buscarPorId(2L)).willThrow(new RuntimeException("ID inválido."));

        // act + assert
        mockMvc.perform(get("/login/{id}", 2L))
                .andExpect(status().isInternalServerError());

        verify(loginService).buscarPorId(2L);
    }

    @Test
    public void testCadastrar() throws Exception {
        // arrange
        LoginDTO request = new LoginDTO();
        request.setUsername("novoUser");

        LoginDTO response = new LoginDTO();
        response.setUsername("novoUser");

        given(loginService.cadastrar(any(LoginDTO.class))).willReturn(response);

        // act + assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("novoUser"));

        ArgumentCaptor<LoginDTO> captor = ArgumentCaptor.forClass(LoginDTO.class);
        verify(loginService).cadastrar(captor.capture());
        assertEquals("novoUser", captor.getValue().getUsername());
    }

    @Test
    public void testAtualizar() throws Exception {
        // arrange
        LoginDTO request = new LoginDTO();
        request.setUsername("atualizado");

        LoginDTO response = new LoginDTO();
        response.setUsername("atualizado");

        given(loginService.atualizar(eq(1L), any(LoginDTO.class))).willReturn(response);

        // act + assert
        mockMvc.perform(put("/login/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("atualizado"));

        verify(loginService).atualizar(eq(1L), any(LoginDTO.class));
    }

    @Test
    public void testDeletar() throws Exception {
        // arrange
        doNothing().when(loginService).deletar(1L);

        // act + assert
        mockMvc.perform(delete("/login/{id}", 1L))
                .andExpect(status().isOk());

        verify(loginService).deletar(1L);
    }
}
*/