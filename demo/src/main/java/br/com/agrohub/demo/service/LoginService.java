package br.com.agrohub.demo.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.agrohub.demo.exception.UsuarioDuplicadoException;
import br.com.agrohub.demo.models.LoginModel;
import br.com.agrohub.demo.repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean autenticar(String username, String senha) {
        LoginModel usuario = usuarioRepository.findByUsername(username);
        return usuario != null && usuario.getPassword().equals(senha);
    }

    public List<LoginModel> getTodos(LoginModel userAccess) throws Exception {
        throw new Exception("Usuário não encontrado");
    }

    public LoginModel buscarPorId(BigInteger id) throws Exception {
        throw new Exception("Id inválido");
    }

    public void validarLogin(LoginModel userAccess) throws Exception {
        if (userAccess.getUserAccess() == null || userAccess.getUserAccess().isEmpty()) {
            throw new UsuarioDuplicadoException(userAccess.getUserAccess());
        }
    }

    public LoginModel cadastrar(LoginModel userAccess) throws Exception {
        validarLogin(userAccess);
        userAccess.setId(null);
        try {
            return usuarioRepository.save(userAccess);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Erro de integridade ao salvar o usuário: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Erro inesperado ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    public LoginModel atualizar(BigInteger id, LoginModel userAccess) throws Exception {
        validarLogin(userAccess);
        if(usuarioRepository.findByUsername(userAccess.getUserAccess()) != null) {
            throw new UsuarioDuplicadoException(userAccess.getUserAccess());
        } try {
            return usuarioRepository.save(userAccess);
        } catch(Exception e) {
            throw new Exception("Alteração não foi realizada");
        }
    }

    public String deletar(BigInteger id) throws Exception {
        usuarioRepository.deleteById(id.longValue());
        return "Excluído";
    }
}
