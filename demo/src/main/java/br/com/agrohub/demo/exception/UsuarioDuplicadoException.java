package br.com.agrohub.demo.exception;

public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String usuario) {
        super("O usuário: " + usuario + " já existe.");
    }
}
