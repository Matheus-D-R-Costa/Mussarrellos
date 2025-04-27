package com.mussarrellos.backend.buildingblocks.application.contracts;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import reactor.core.publisher.Mono;

public interface IModule {

    <TResult> Mono<TResult> executeCommand(ICommand<TResult> command);

    Mono<Void> executeCommand(ICommandWithoutResult command);

    <TResult> Mono<TResult> executeQuery(IQuery<TResult> query);

} 