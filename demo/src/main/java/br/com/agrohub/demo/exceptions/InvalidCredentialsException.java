package br.com.agrohub.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para falhas de autenticação (email/senha incorretos).
 * Retorna o status HTTP 401 (Unauthorized) ou 403 (Forbidden).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED) // Status 401
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}