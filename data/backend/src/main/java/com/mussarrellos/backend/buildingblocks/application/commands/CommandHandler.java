package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

/**
 * Interface genérica para handlers de comandos.
 * Segue o padrão Command do CQRS.
 *
 * @param <C> Tipo do comando
 * @param <R> Tipo do resultado
 */
public interface CommandHandler<C, R> {
    /**
     * Processa o comando e retorna o resultado.
     *
     * @param command Comando a ser processado
     * @return Resultado do processamento
     */
    Mono<R> handle(C command);
} 