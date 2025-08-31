package br.com.agrohub.demo.exception;

public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String nomeUsuario) {
        super("O usuário: " + nomeUsuario + " já existe.");
    }
}
