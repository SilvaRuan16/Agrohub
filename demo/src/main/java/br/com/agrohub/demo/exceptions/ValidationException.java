package br.com.agrohub.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para erros de validação de negócio ou de entrada de dados.
 * Retorna o status HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // Status 400
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}