package com.mussarrellos.backend.modules.user.application.queries.handlers;

import com.mussarrellos.backend.buildingblocks.application.queries.QueryHandler;
import com.mussarrellos.backend.modules.user.application.dtos.ClientDto;
import com.mussarrellos.backend.modules.user.application.queries.GetClientByIdQuery;
import com.mussarrellos.backend.modules.user.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.user.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handler para a query de busca de cliente por ID.
 * Transforma a entidade em DTO para exposição externa.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GetClientByIdQueryHandler implements QueryHandler<GetClientByIdQuery, ClientDto> {

    private final ClientRepository repository;

    @Override
    public Mono<ClientDto> handle(GetClientByIdQuery query) {
        log.debug("Handling GetClientByIdQuery for ID: {}", query.clientId());
        
        return repository.findById(new ClientId(query.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + query.clientId())))
            .map(client -> new ClientDto(
                client.getId().getValue(),
                client.getEmail(),
                client.getRegistrationDate(),
                client.getEmailUpdatedDate(),
                client.getPasswordUpdatedDate()
            ))
            .doOnSuccess(dto -> log.debug("Found client with ID: {}", dto.id()))
            .doOnError(error -> log.error("Error finding client: {}", error.getMessage()));
    }
} 