package com.mussarrellos.backend.buildingblocks.application.mediator;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import reactor.core.publisher.Mono;

/**
 * Interface do padrão Mediator para comunicação entre componentes.
 * Fornece um ponto centralizado para envio de comandos e consultas.
 */
public interface Mediator {
    
    /**
     * Envia um comando que retorna um resultado.
     *
     * @param command O comando a ser enviado
     * @param <T> O tipo do resultado
     * @return Um Mono contendo o resultado do processamento do comando
     */
    <T> Mono<T> send(ICommand<T> command);
    
    /**
     * Envia um comando que não retorna resultado.
     *
     * @param command O comando a ser enviado
     * @return Um Mono vazio que sinaliza a conclusão do processamento
     */
    Mono<Void> send(ICommandWithoutResult command);
    
    /**
     * Envia uma consulta.
     *
     * @param query A consulta a ser enviada
     * @param <T> O tipo do resultado
     * @return Um Mono contendo o resultado da consulta
     */
    <T> Mono<T> send(IQuery<T> query);
} 