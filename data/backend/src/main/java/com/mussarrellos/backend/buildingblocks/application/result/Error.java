package com.mussarrellos.backend.buildingblocks.application.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representa um erro de negócio.
 * Utilizado pelo padrão Result para comunicar erros sem usar exceções para fluxos esperados.
 */
@Getter
@RequiredArgsConstructor
public class Error {
    /**
     * A mensagem de erro.
     */
    private final String message;
    
    /**
     * O código de erro, opcional.
     */
    private final String code;
    
    /**
     * Cria um erro com a mensagem fornecida e sem código.
     *
     * @param message A mensagem de erro
     */
    public Error(String message) {
        this(message, null);
    }
    
    /**
     * Verifica se este erro tem um código associado.
     *
     * @return true se o erro tem um código, false caso contrário
     */
    public boolean hasCode() {
        return code != null && !code.isEmpty();
    }
    
    @Override
    public String toString() {
        return hasCode() ? 
            String.format("[%s] %s", code, message) :
            message;
    }
} 