package com.mussarrellos.backend.buildingblocks.application.queries;

import reactor.core.publisher.Mono;

public interface IQueryHandler<TQuery extends IQuery<TResult>, TResult> extends QueryHandler<TQuery, TResult> {

    @Override
    Mono<TResult> handle(TQuery query);

} 