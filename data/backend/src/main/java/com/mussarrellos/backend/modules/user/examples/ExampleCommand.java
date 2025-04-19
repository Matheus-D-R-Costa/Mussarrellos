package com.mussarrellos.backend.modules.user.examples;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * EXEMPLO: Comando para criar um novo recurso.
 * 
 * Comandos devem ser imutáveis e conter apenas os dados necessários
 * para a operação ser executada.
 */
@Value
public class ExampleCommand implements ICommand<UUID> {
    String name;
    String description;
}

/**
 * EXEMPLO: Handler para o comando de criação de recurso.
 * 
 * Handlers implementam a lógica de negócio e processam comandos.
 * Devem ser anotados com @Component para serem detectados pelo IUserModule.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ExampleCommandHandler implements ICommandHandler<ExampleCommand, UUID> {
    
    // Injetar repositórios e serviços necessários
    // private final ExampleRepository repository;
    
    @Override
    public Mono<UUID> handle(ExampleCommand command) {
        log.debug("Handling ExampleCommand with name: {}", command.getName());
        
        // Implementação da lógica de negócio
        return Mono.just(UUID.randomUUID())
            .doOnSuccess(id -> log.debug("Created resource with ID: {}", id))
            .doOnError(error -> log.error("Error creating resource: {}", error.getMessage()));
    }
} 