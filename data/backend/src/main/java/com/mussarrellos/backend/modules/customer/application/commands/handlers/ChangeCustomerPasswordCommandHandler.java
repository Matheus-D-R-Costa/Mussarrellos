package com.mussarrellos.backend.modules.customer.application.commands.handlers;

import com.mussarrellos.backend.buildingblocks.application.commands.CommandHandler;
import com.mussarrellos.backend.modules.customer.application.commands.ChangeCustomerPasswordCommand;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ChangeCustomerPasswordCommandHandler implements CommandHandler<ChangeCustomerPasswordCommand, Void> {
    private final CustomerRepository customerRepository;

    @Override
    public Mono<Void> handle(ChangeCustomerPasswordCommand command) {
        log.debug("Processando comando ChangeClientPasswordCommand para cliente ID: {}", command.clientId());
        
        return customerRepository.findById(new CustomerId(command.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + command.clientId())))
            .flatMap(client -> {
                try {
                    // Altera a senha do cliente
                    client.changePassword(
                        command.currentPassword(),
                        command.newPassword()
                    );
                    
                    // Salva as alterações
                    return customerRepository.save(client);
                } catch (IllegalArgumentException e) {
                    return Mono.error(e);
                }
            })
            .then() // Converte para Mono<Void>
            .doOnSuccess(v -> log.debug("Senha do cliente alterada com sucesso para cliente ID: {}", command.clientId()))
            .doOnError(error -> log.error("Erro ao alterar senha do cliente: {}", error.getMessage()));
    }
} 