package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

public interface ICommandWithoutResultHandler<TCommand extends ICommandWithoutResult> extends CommandHandler<TCommand, Void> {

    @Override
    Mono<Void> handle(TCommand command);

} 