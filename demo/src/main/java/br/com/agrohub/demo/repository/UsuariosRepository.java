package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Usuarios findByEmail(String email);

    Usuarios findByEmailStartingWithIgnoreCase(String email);

    Usuarios findByEmailEndingWithIgnoreCase(String email);

    Usuarios findByEmailContainingIgnoreCase(String email);

    @Query("SELECT e FROM Usuarios e WHERE e.email LIKE %?1%")
    Usuarios findByMinhaQuery(String email);

    boolean existsByEmail (String email);
}