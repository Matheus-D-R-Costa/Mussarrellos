package com.mussarrellos.backend.modules.user.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResultHandler;
import com.mussarrellos.backend.modules.user.application.commands.ChangeClientEmailCommand;
import com.mussarrellos.backend.modules.user.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.user.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para o comando de alteração de email do cliente.
 * Localiza o cliente e atualiza seu email.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeClientEmailCommandHandler implements ICommandWithoutResultHandler<ChangeClientEmailCommand> {

    private final ClientRepository repository;

    @Override
    public Mono<Void> handle(ChangeClientEmailCommand command) {
        log.debug("Handling ChangeClientEmailCommand for client ID: {}", command.clientId());
        
        return repository.findById(new ClientId(command.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + command.clientId())))
            .flatMap(client -> {
                client.changeEmail(
                    command.newEmail(),
                    email -> repository.checkEmailUniqueness(email).block()
                );
                
                return repository.save(client).then();
            })
            .doOnSuccess(v -> log.debug("Client email changed successfully for ID: {}", command.clientId()))
            .doOnError(error -> log.error("Error changing client email: {}", error.getMessage()));
    }
} 