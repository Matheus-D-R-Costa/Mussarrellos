package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

/**
 * Interface genérica para handlers de comandos que implementam ICommand.
 * Facilita a criação de handlers para comandos que seguem a nova abordagem.
 *
 * @param <TCommand> Tipo do comando que implementa ICommand
 * @param <TResult> Tipo do resultado
 */
public interface ICommandHandler<TCommand extends ICommand<TResult>, TResult> extends CommandHandler<TCommand, TResult> {
    
    /**
     * Processa o comando e retorna o resultado.
     *
     * @param command Comando a ser processado
     * @return Resultado do processamento
     */
    @Override
    Mono<TResult> handle(TCommand command);
} 