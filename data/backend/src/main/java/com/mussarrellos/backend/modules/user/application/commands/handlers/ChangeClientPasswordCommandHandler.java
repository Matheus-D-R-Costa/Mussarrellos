package com.mussarrellos.backend.modules.user.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.CommandHandler;
import com.mussarrellos.backend.modules.user.application.commands.ChangeClientPasswordCommand;
import com.mussarrellos.backend.modules.user.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.user.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para o comando de alteração de senha do cliente.
 * Coordena a busca, alteração e persistência do cliente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeClientPasswordCommandHandler implements CommandHandler<ChangeClientPasswordCommand, Void> {
    private final ClientRepository clientRepository;

    @Override
    public Mono<Void> handle(ChangeClientPasswordCommand command) {
        log.debug("Handling ChangeClientPasswordCommand for client ID: {}", command.clientId());
        
        return clientRepository.findById(new ClientId(command.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + command.clientId())))
            .flatMap(client -> {
                try {
                    // Altera a senha do cliente
                    client.changePassword(
                        command.currentPassword(),
                        command.newPassword()
                    );
                    
                    // Salva as alterações
                    return clientRepository.save(client);
                } catch (IllegalArgumentException e) {
                    return Mono.error(e);
                }
            })
            .then() // Converte para Mono<Void>
            .doOnSuccess(v -> log.debug("Client password changed successfully for client ID: {}", command.clientId()))
            .doOnError(error -> log.error("Error changing client password: {}", error.getMessage()));
    }
} 