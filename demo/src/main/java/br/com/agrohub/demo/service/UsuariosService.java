package br.com.agrohub.demo.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agrohub.demo.dto.UsuarioDTO;
import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.Usuarios;
import br.com.agrohub.demo.repository.UsuariosRepository;

@Service
public class UsuariosService extends GenericService<Usuarios, UsuarioDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected UsuarioDTO toDTO(Usuarios entity) {
        UsuarioDTO dto = mapper.map(entity, UsuarioDTO.class);
        if (dto != null) {
            dto.setSenha(null);
        }
        return dto;
    }

    @Override
    protected Usuarios toEntity(UsuarioDTO dto) {
        return mapper.map(dto, Usuarios.class);
    }

    public boolean autenticar(String email, String senha) {
        Usuarios usuario = usuariosRepository.findByEmail(email);
        if (usuario == null || usuario.getSenha() == null) {
            return false;
        }
        return passwordEncoder.matches(senha, usuario.getSenha());
    }

    @GetMapping
    public UsuarioDTO getTodos(@RequestParam UsuarioDTO dto2) {
        Usuarios usuario = usuariosRepository.findByEmail(dto2.getEmail());
        UsuarioDTO dto = this.toDTO(usuario);
        return dto;
    }

    public UsuarioDTO buscarPorId(Long id_usuario) throws Exception {
        Optional<Usuarios> usuario = usuariosRepository.findById(id_usuario);
        if (!usuario.isPresent()) {
            throw new Exception("ID inválido.");
        }
        return toDTO(usuario.get());
    }

    public void validarLogin(UsuarioDTO dto) throws Exception {
        if (usuariosRepository.existsByEmail(dto.getEmail())) {
            throw new UsuarioDuplicadoException(dto.getEmail());
        }
    }

    public UsuarioDTO cadastrar(UsuarioDTO entityDTO) throws Exception {
        validarLogin(entityDTO);
        entityDTO.setId_usuario(null);

        if (entityDTO.getSenha() == null || entityDTO.getSenha().isBlank()) {
            throw new Exception("Senha não informada.");
        }

        entityDTO.setSenha(passwordEncoder.encode(entityDTO.getSenha()));

        try {
            return toDTO(usuariosRepository.save(toEntity(entityDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Erro de integridade ao salvar o usuário: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Erro inesperado ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    public UsuarioDTO atualizar(Long id_usuario, UsuarioDTO newData) throws Exception {
        Usuarios existing = usuariosRepository.findById(id_usuario)
                .orElseThrow(() -> new Exception("ID inválido."));

        if (!existing.getEmail().equals(newData.getEmail()) &&
                usuariosRepository.existsByEmail(newData.getEmail())) {
            throw new UsuarioDuplicadoException(newData.getEmail());
        }

        if (newData.getSenha() == null || newData.getSenha().isBlank()) {
            newData.setSenha(existing.getSenha()); // mantém hash atual
        } else {

            newData.setSenha(encodeIfNeeded(newData.getSenha()));
        }

        newData.setId_usuario(id_usuario);

        try {
            return toDTO(usuariosRepository.save(toEntity(newData)));
        } catch (Exception e) {
            throw new Exception("A alteração não foi realizada: " + e.getMessage(), e);
        }
    }

    public String deletar(Long id) throws Exception {
        usuariosRepository.deleteById(id);
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
