package com.mussarrellos.backend.modules.customer.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResultHandler;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeCustomerEmailCommand;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ChangeCustomerEmailCommandHandler implements ICommandWithoutResultHandler<ChangeCustomerEmailCommand> {

    private final CustomerRepository repository;

    @Override
    public Mono<Void> handle(ChangeCustomerEmailCommand command) {
        log.debug("Processando comando ChangeClientEmailCommand para cliente ID: {}", command.clientId());
        
        return repository.findById(new CustomerId(command.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + command.clientId())))
            .flatMap(client -> {
                client.changeEmail(
                    command.newEmail(),
                    email -> repository.checkEmailUniqueness(email).block()
                );
                
                return repository.save(client).then();
            })
            .doOnSuccess(v -> log.debug("Email do cliente alterado com sucesso para ID: {}", command.clientId()))
            .doOnError(error -> log.error("Erro ao alterar email do cliente: {}", error.getMessage()));
    }
} 