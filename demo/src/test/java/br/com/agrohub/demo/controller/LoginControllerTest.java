// package br.com.agrohub.demo.controller;

// import static org.hamcrest.Matchers.containsString;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import br.com.agrohub.demo.models.LoginModel;
// import br.com.agrohub.demo.repository.UsuarioRepository;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// public class LoginControllerTest {

// @Autowired
// MockMvc mockMvc;

// @MockitoBean
// UsuarioRepository usuarioRepository;

// @Test
// @DisplayName("Testando o retorno do get para buscar todos os logins no
// endpoint /login")
// @WithMockUser(username = "test", roles = { "USER" })
// void testGetTodos() throws Exception {
// // prepara um exemplo de entidade e uma Page com totalElements = 1
// LoginModel lm = new LoginModel();
// lm.setId(1L);
// lm.setUsername("user");
// lm.setPassword("$2a$hash");

// Page<LoginModel> page = new PageImpl<>(List.of(lm), PageRequest.of(0, 10),
// 1);
// // stub comum: adapte se seu controller chama outro método do
// repository/service
// when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(page);
// // caso o controller invoque findAll() sem pageable:
// when(usuarioRepository.findAll()).thenReturn(List.of(lm));

// mockMvc.perform(get("/login"))
// .andExpect(status().isOk())
// .andExpect(content().string(containsString("totalElements")));
// }
// }
