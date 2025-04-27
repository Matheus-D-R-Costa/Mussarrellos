package com.mussarrellos.backend.modules.customer.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.modules.customer.application.commands.RegisterCustomerCommand;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class RegisterCustomerCommandHandler implements ICommandHandler<RegisterCustomerCommand, UUID> {

    private final CustomerRepository repository;

    @Override
    public Mono<UUID> handle(RegisterCustomerCommand command) {
        log.debug("Processando comando RegisterClientCommand: {}", command.email());
        return Mono.fromCallable(() -> {
            // fazer a criação do bichinho...
        }).flatMap(repository::save
        ).doOnSuccess(clientId -> 
            log.debug("Cliente registrado com sucesso com ID: {}", clientId)
        ).doOnError(error -> 
            log.error("Erro ao registrar cliente: {}", error.getMessage())
        );
    }
} 