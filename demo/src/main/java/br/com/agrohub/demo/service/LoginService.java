package br.com.agrohub.demo.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agrohub.demo.dto.LoginDTO;
import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.LoginModel;
import br.com.agrohub.demo.repository.UsuarioRepository;

@Service
public class LoginService extends GenericService<LoginModel, LoginDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected LoginDTO toDTO(LoginModel entity) {
        LoginDTO dto = mapper.map(entity, LoginDTO.class);
        if (dto != null) {
            dto.setPassword(null);
        }
        return dto;
    }

    @Override
    protected LoginModel toEntity(LoginDTO dto) {
        return mapper.map(dto, LoginModel.class);
    }

    public boolean autenticar(String username, String senha) {
        LoginModel usuario = usuarioRepository.findByUsername(username);
        if (usuario == null || usuario.getPassword() == null) {
            return false;
        }
        return passwordEncoder.matches(senha, usuario.getPassword());
    }

    // Correct method returning LoginDTO
    @GetMapping
    public LoginDTO getTodos(@RequestParam LoginDTO dto2) {
        LoginModel usuario = usuarioRepository.findByUsername(dto2.getUsername());
        LoginDTO dto = this.toDTO(usuario);
        return dto;
    }

    public LoginDTO buscarPorId(Long id) throws Exception {
        Optional<LoginModel> usuario = usuarioRepository.findById(id);
        if (!usuario.isPresent()) {
            throw new Exception("ID inválido.");
        }
        return toDTO(usuario.get());
    }

    public void validarLogin(LoginDTO dto) throws Exception {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new UsuarioDuplicadoException(dto.getUsername());
        }
    }

    public LoginDTO cadastrar(LoginDTO entityDTO) throws Exception {
        validarLogin(entityDTO);
        entityDTO.setId(null);

        if (entityDTO.getPassword() == null || entityDTO.getPassword().isBlank()) {
            throw new Exception("Senha não informada.");
        }

        entityDTO.setPassword(passwordEncoder.encode(entityDTO.getPassword()));

        try {
            return toDTO(usuarioRepository.save(toEntity(entityDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Erro de integridade ao salvar o usuário: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Erro inesperado ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    public LoginDTO atualizar(Long id, LoginDTO newData) throws Exception {
        LoginModel existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("ID inválido."));

        if (!existing.getUsername().equals(newData.getUsername()) &&
                usuarioRepository.existsByUsername(newData.getUsername())) {
            throw new UsuarioDuplicadoException(newData.getUsername());
        }

        if (newData.getPassword() == null || newData.getPassword().isBlank()) {
            newData.setPassword(existing.getPassword()); // mantém hash atual
        } else {

            newData.setPassword(encodeIfNeeded(newData.getPassword()));
        }

        newData.setId(id);

        try {
            return toDTO(usuarioRepository.save(toEntity(newData)));
        } catch (Exception e) {
            throw new Exception("A alteração não foi realizada: " + e.getMessage(), e);
        }
    }

    public String deletar(Long id) throws Exception {
        usuarioRepository.deleteById(id);
        return "Excluído";
    }

    private String encodeIfNeeded(String passwordOrHash) {
        if (passwordOrHash == null)
            return null;
        String trimmed = passwordOrHash.trim();
        if (trimmed.startsWith("$2a$") || trimmed.startsWith("$2b$") || trimmed.startsWith("$2y$")) {
            return trimmed;
        }
        return passwordEncoder.encode(trimmed);
    }
}
