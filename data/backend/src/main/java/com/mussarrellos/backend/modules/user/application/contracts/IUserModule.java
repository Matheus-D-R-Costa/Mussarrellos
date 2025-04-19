package com.mussarrellos.backend.modules.user.application.contracts;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import reactor.core.publisher.Mono;

/**
 * Interface principal do módulo de usuários.
 * Define as operações para execução de comandos e queries no módulo.
 */
public interface IUserModule {
    
    /**
     * Executa um comando que retorna um resultado.
     *
     * @param command O comando a ser executado
     * @param <TResult> Tipo do resultado
     * @return Mono contendo o resultado da execução do comando
     */
    <TResult> Mono<TResult> executeCommand(ICommand<TResult> command);

    /**
     * Executa um comando que não retorna resultado.
     *
     * @param command O comando a ser executado
     * @return Mono completado quando o comando for executado
     */
    Mono<Void> executeCommand(ICommandWithoutResult command);

    /**
     * Executa uma query.
     *
     * @param query A query a ser executada
     * @param <TResult> Tipo do resultado
     * @return Mono contendo o resultado da execução da query
     */
    <TResult> Mono<TResult> executeQuery(IQuery<TResult> query);
} 