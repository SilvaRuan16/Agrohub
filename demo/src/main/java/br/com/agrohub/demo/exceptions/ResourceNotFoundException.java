package br.com.agrohub.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para quando um recurso (entidade) não é encontrado.
 * Retorna o status HTTP 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Status 404
public class ResourceNotFoundException extends RuntimeException {

    // Construtor para casos gerais
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " com ID " + id + " não encontrado(a).");
    }
    
    // Construtor opcional para casos de busca por outro campo (ex: email)
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(resourceName + " com identificador '" + identifier + "' não encontrado(a).");
    }

    // Construtor padrão (menos recomendado para REST)
    public ResourceNotFoundException(String message) {
        super(message);
    }
}