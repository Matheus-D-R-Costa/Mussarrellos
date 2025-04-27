package com.mussarrellos.backend.buildingblocks.application.commands;

import reactor.core.publisher.Mono;

public interface ICommandHandler<TCommand extends ICommand<TResult>, TResult> extends CommandHandler<TCommand, TResult> {

    @Override
    Mono<TResult> handle(TCommand command);

} 