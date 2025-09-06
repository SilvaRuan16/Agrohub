package br.com.agrohub.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.dto.LoginDTO;
import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.LoginModel;
import br.com.agrohub.demo.repository.UsuarioRepository;

@Service
public class LoginService extends GenericService<LoginModel, LoginDTO> {

    @Autowired
    private ModelMapper mapper;

    @Override
    protected LoginDTO toDTO(LoginModel entity) {
        return mapper.map(entity, LoginDTO.class);
    }

    @Override
    protected LoginModel toEntity(LoginDTO dto) {
        return mapper.map(dto, LoginModel.class);
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean autenticar(String username, String senha) {
        LoginModel usuario = usuarioRepository.findByUsername(username);
        return usuario != null && usuario.getPassword().equals(senha);
    }

    public LoginDTO getTodos(LoginDTO entityDTO) throws Exception {
        if (entityDTO == null || entityDTO.getUsername() == null) {
            throw new Exception("Usuário não informado.");
        }

        LoginModel usuario = usuarioRepository.findByUsername(entityDTO.getUsername());
        if (usuario == null) {
            throw new Exception("Cliente não encontrado.");
        }
        return toDTO(usuario);
    }

    public LoginDTO buscarPorId(Long id) throws Exception {
        return toDTO(usuarioRepository.findById(id).orElseThrow(
                () -> new Exception("ID inválido.")));
    }

    public void validarLogin(LoginDTO dto) throws Exception {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new UsuarioDuplicadoException(dto.getUsername());
        }
    }

    public LoginDTO cadastrar(LoginDTO entityDTO) throws Exception {
        validarLogin(entityDTO);
        entityDTO.setId(null);
        try {
            return toDTO(usuarioRepository.save(toEntity(entityDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Erro de integridade ao salvar o usuário: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Erro inesperado ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    public LoginDTO atualizar(Long id, LoginDTO newData) throws Exception {
        validarLogin(newData);
        if (usuarioRepository.findByUsername(newData.getUsername()) != null) {
            throw new UsuarioDuplicadoException(newData.getUsername());
        }
        try {
            return toDTO(usuarioRepository.save(toEntity(newData)));
        } catch (Exception e) {
            throw new Exception("A alteração não foi realizada");
        }
    }

    public String deletar(Long id) throws Exception {
        usuarioRepository.deleteById(id);
        return "Excluído";
    }
}
