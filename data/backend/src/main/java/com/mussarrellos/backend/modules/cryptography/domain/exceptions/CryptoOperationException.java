package com.mussarrellos.backend.modules.cryptography.domain.exceptions;

/**
 * Exceção lançada quando ocorre algum erro em operações criptográficas.
 * Fornece uma camada de abstração sobre as exceções específicas da JCA.
 */
public class CryptoOperationException extends RuntimeException {

    /**
     * Cria uma nova exceção com a mensagem fornecida.
     *
     * @param message A mensagem de erro
     */
    public CryptoOperationException(String message) {
        super(message);
    }

    /**
     * Cria uma nova exceção com a mensagem e causa fornecidas.
     *
     * @param message A mensagem de erro
     * @param cause A causa raiz da exceção
     */
    public CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 