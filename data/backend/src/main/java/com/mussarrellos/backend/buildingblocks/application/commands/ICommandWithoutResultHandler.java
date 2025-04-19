package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

/**
 * Interface genérica para handlers de comandos sem resultado que implementam ICommandWithoutResult.
 * Facilita a criação de handlers para comandos que seguem a nova abordagem.
 *
 * @param <TCommand> Tipo do comando que implementa ICommandWithoutResult
 */
public interface ICommandWithoutResultHandler<TCommand extends ICommandWithoutResult> extends CommandHandler<TCommand, Void> {
    
    /**
     * Processa o comando sem retornar resultado.
     *
     * @param command Comando a ser processado
     * @return Mono completado quando o comando for executado
     */
    @Override
    Mono<Void> handle(TCommand command);
} 