package br.com.agrohub.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
