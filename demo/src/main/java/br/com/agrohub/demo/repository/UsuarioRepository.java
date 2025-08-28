package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.agrohub.demo.models.LoginModel;

public interface UsuarioRepository extends JpaRepository<LoginModel, Long> {
    LoginModel findByUsername(String username);
}