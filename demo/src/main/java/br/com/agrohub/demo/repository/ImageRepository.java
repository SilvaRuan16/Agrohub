package br.com.agrohub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agrohub.demo.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // O JpaRepository já fornece todos os métodos de CRUD (save, findById, findAll, etc.).
    // Métodos customizados podem ser adicionados aqui, se necessário.
}