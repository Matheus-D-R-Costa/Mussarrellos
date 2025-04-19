package com.mussarrellos.backend.buildingblocks.application.queries;

import reactor.core.publisher.Mono;

/**
 * Interface genérica para handlers de queries que implementam IQuery.
 * Facilita a criação de handlers para queries que seguem a nova abordagem.
 *
 * @param <TQuery> Tipo da query que implementa IQuery
 * @param <TResult> Tipo do resultado
 */
public interface IQueryHandler<TQuery extends IQuery<TResult>, TResult> extends QueryHandler<TQuery, TResult> {
    
    /**
     * Processa a query e retorna o resultado.
     *
     * @param query Query a ser processada
     * @return Resultado da consulta
     */
    @Override
    Mono<TResult> handle(TQuery query);
} 