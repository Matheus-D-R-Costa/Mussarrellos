package com.mussarrellos.backend.buildingblocks.application.queries;

import reactor.core.publisher.Mono;

public interface QueryHandler<Q, R> {

    Mono<R> handle(Q query);

} 