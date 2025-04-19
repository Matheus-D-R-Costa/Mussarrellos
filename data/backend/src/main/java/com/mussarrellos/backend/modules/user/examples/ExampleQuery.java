package com.mussarrellos.backend.modules.user.examples;

import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.buildingblocks.application.queries.IQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * EXEMPLO: DTO para retorno de dados.
 * 
 * DTOs devem ser imutáveis e conter apenas os dados necessários
 * para a exibição na camada de apresentação.
 */
@Value
class ExampleDto {
    UUID id;
    String name;
    String description;
    LocalDateTime createdAt;
}

/**
 * EXEMPLO: Query para buscar um recurso pelo ID.
 * 
 * Queries devem ser imutáveis e conter apenas os dados necessários
 * para a consulta ser executada.
 */
@Value
public class ExampleQuery implements IQuery<ExampleDto> {
    UUID id;
}

/**
 * EXEMPLO: Handler para a query de busca de recurso.
 * 
 * Handlers implementam a lógica de consulta e processam queries.
 * Devem ser anotados com @Component para serem detectados pelo IUserModule.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ExampleQueryHandler implements IQueryHandler<ExampleQuery, ExampleDto> {
    
    // Injetar repositórios e serviços necessários
    // private final ExampleRepository repository;
    
    @Override
    public Mono<ExampleDto> handle(ExampleQuery query) {
        log.debug("Handling ExampleQuery for ID: {}", query.getId());
        
        // Implementação da lógica de consulta
        // Em um cenário real, buscaria no repositório
        return Mono.just(
                new ExampleDto(
                    query.getId(),
                    "Nome do recurso",
                    "Descrição do recurso",
                    LocalDateTime.now()
                )
            )
            .doOnSuccess(dto -> log.debug("Found resource with ID: {}", dto.getId()))
            .doOnError(error -> log.error("Error finding resource: {}", error.getMessage()));
    }
} 