package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Contatos;

@Repository
public interface ContatosRepository extends JpaRepository<Contatos, Long> {

    Contatos findByEmail(String email);

    Contatos findByEmailStartingWithIgnoreCase(String email);

    Contatos findByEmailEndingWithIgnoreCase(String email);

    Contatos findByEmailContainingIgnoreCase(String email);

    @Query("SELECT e FROM Contatos e WHERE e.email LIKE %?1%")
    Contatos findByMinhaQuery(String email);

    boolean existsByEmail (String email);
}