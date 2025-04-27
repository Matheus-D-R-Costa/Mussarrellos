package com.mussarrellos.backend.buildingblocks.application.mediator;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import reactor.core.publisher.Mono;

public interface Mediator {

    <T> Mono<T> send(ICommand<T> command);

    Mono<Void> send(ICommandWithoutResult command);

    <T> Mono<T> send(IQuery<T> query);
} 