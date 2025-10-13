package br.com.agrohub.demo.repository; // Pacote conforme solicitado

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agrohub.demo.models.Comment;

// Repositório que gerencia os comentários/avaliações dos produtos.
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Método customizado crucial para a tela de detalhes do produto (h2.png):
    // Buscar todos os comentários para um produto específico (pelo ID do produto)
    List<Comment> findByProductId(Long productId);
    
    // Buscar todos os comentários de um determinado usuário
    List<Comment> findByUserId(Long userId);
}
