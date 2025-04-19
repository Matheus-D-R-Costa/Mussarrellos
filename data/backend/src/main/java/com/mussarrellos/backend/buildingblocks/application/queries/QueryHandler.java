package com.mussarrellos.backend.buildingblocks.application.queries;

import reactor.core.publisher.Mono;

/**
 * Interface genérica para handlers de queries (consultas).
 * Segue o padrão Query do CQRS.
 *
 * @param <Q> Tipo da query
 * @param <R> Tipo do resultado
 */
public interface QueryHandler<Q, R> {
    /**
     * Processa a query e retorna o resultado.
     *
     * @param query Query a ser processada
     * @return Resultado da consulta
     */
    Mono<R> handle(Q query);
} 