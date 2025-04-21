package com.mussarrellos.backend.buildingblocks.application.mediator;

import com.mussarrellos.backend.buildingblocks.application.commands.CommandHandler;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResultHandler;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.buildingblocks.application.queries.IQueryHandler;
import com.mussarrellos.backend.buildingblocks.application.queries.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementação do Mediator usando o Spring para resolução de handlers.
 * Permite o desacoplamento entre remetentes e destinatários de mensagens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringMediator implements Mediator {

    private final ApplicationContext applicationContext;

    @Override
    public <T> Mono<T> send(ICommand<T> command) {
        log.debug("Mediating command: {}", command.getClass().getSimpleName());
        return findCommandHandler(command)
                .flatMap(handler -> handler.handle(command));
    }

    @Override
    public Mono<Void> send(ICommandWithoutResult command) {
        log.debug("Mediating command without result: {}", command.getClass().getSimpleName());
        return findCommandWithoutResultHandler(command)
                .flatMap(handler -> handler.handle(command));
    }

    @Override
    public <T> Mono<T> send(IQuery<T> query) {
        log.debug("Mediating query: {}", query.getClass().getSimpleName());
        return findQueryHandler(query)
                .flatMap(handler -> handler.handle(query));
    }

    @SuppressWarnings("unchecked")
    private <T> Mono<ICommandHandler<ICommand<T>, T>> findCommandHandler(ICommand<T> command) {
        return Flux.fromIterable(applicationContext.getBeansOfType(ICommandHandler.class).values())
                .filter(handler -> isHandlerForCommand(handler, command))
                .next()
                .cast(ICommandHandler.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "No handler found for command: " + command.getClass().getSimpleName())))
                .cast(ICommandHandler.class);
    }

    @SuppressWarnings("unchecked")
    private Mono<ICommandWithoutResultHandler<ICommandWithoutResult>> findCommandWithoutResultHandler(ICommandWithoutResult command) {
        return Flux.fromIterable(applicationContext.getBeansOfType(ICommandWithoutResultHandler.class).values())
                .filter(handler -> isHandlerForCommandWithoutResult(handler, command))
                .next()
                .cast(ICommandWithoutResultHandler.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "No handler found for command without result: " + command.getClass().getSimpleName())));
    }

    @SuppressWarnings("unchecked")
    private <T> Mono<IQueryHandler<IQuery<T>, T>> findQueryHandler(IQuery<T> query) {
        return Flux.fromIterable(applicationContext.getBeansOfType(IQueryHandler.class).values())
                .filter(handler -> isHandlerForQuery(handler, query))
                .next()
                .cast(IQueryHandler.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "No handler found for query: " + query.getClass().getSimpleName())));
    }

    private <T> boolean isHandlerForCommand(Object handler, ICommand<T> command) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                handler.getClass(), ICommandHandler.class);
        return generics != null && generics.length > 0 && 
                generics[0].isAssignableFrom(command.getClass());
    }

    private boolean isHandlerForCommandWithoutResult(Object handler, ICommandWithoutResult command) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                handler.getClass(), ICommandWithoutResultHandler.class);
        return generics != null && generics.length > 0 && 
                generics[0].isAssignableFrom(command.getClass());
    }

    private <T> boolean isHandlerForQuery(Object handler, IQuery<T> query) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                handler.getClass(), IQueryHandler.class);
        return generics != null && generics.length > 0 && 
                generics[0].isAssignableFrom(query.getClass());
    }
} 