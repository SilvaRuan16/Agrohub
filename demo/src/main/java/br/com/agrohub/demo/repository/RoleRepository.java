package br.com.agrohub.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método essencial: buscar uma Role pelo seu nome (ex: ROLE_CLIENTE, ROLE_EMPRESA)
    Optional<Role> findByNome(String nome);
}
