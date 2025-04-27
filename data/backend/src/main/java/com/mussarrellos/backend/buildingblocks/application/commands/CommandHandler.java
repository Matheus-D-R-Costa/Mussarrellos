package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

public interface CommandHandler<C, R> {

    Mono<R> handle(C command);

} 