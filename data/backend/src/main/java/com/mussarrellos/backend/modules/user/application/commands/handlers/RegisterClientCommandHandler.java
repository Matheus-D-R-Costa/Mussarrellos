package com.mussarrellos.backend.modules.user.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import com.mussarrellos.backend.modules.user.application.commands.RegisterClientCommand;
import com.mussarrellos.backend.modules.user.domain.entity.Client;
import com.mussarrellos.backend.modules.user.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Handler para o comando de registro de cliente.
 * Coordena a criação e persistência de um novo cliente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterClientCommandHandler implements ICommandHandler<RegisterClientCommand, UUID> {

    private final ClientRepository repository;

    @Override
    public Mono<UUID> handle(RegisterClientCommand command) {
        log.debug("Handling RegisterClientCommand: {}", command.email());
        return Mono.fromCallable(() -> {
            // Converter reativo para bloqueante apenas na camada de aplicação


            return Client.create(
                    command.email(),
                    command.password(),
                    email -> Boolean.TRUE.equals(repository.checkEmailUniqueness(email).block()) // Converter reativo para bloqueante apenas na camada de aplicação
            );
        }).flatMap(repository::save
        ).doOnSuccess(clientId -> 
            log.debug("Client registered successfully with ID: {}", clientId)
        ).doOnError(error -> 
            log.error("Error registering client: {}", error.getMessage())
        );
    }
} 