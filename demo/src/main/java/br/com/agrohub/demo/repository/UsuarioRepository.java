package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.LoginModel;

@Repository
public interface UsuarioRepository extends JpaRepository<LoginModel, Long> {

    LoginModel findByUsername(String username);

    LoginModel findByUsernameStartingWithIgnoreCase(String username);

    LoginModel findByUsernameEndingWithIgnoreCase(String username);

    LoginModel findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT e FROM LoginModel e WHERE e.username LIKE %?1%")
    LoginModel findByMinhaQuery(String username);

    boolean existsByUsername (String username);
}