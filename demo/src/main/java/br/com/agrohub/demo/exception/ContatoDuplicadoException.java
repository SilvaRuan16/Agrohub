package br.com.agrohub.demo.exception;

public class ContatoDuplicadoException extends RuntimeException {
    public ContatoDuplicadoException(String contatoEmail) {
        super("O contato: " + contatoEmail + " já existe.");
    }
}
