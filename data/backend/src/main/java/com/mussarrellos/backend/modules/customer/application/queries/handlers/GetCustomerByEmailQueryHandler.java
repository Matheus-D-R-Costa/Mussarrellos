package com.mussarrellos.backend.modules.customer.application.queries.handlers;

import com.mussarrellos.backend.buildingblocks.application.queries.IQueryHandler;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;
import com.mussarrellos.backend.modules.customer.application.queries.GetCustomerByEmailQuery;
import com.mussarrellos.backend.modules.customer.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Handler para a query de busca de cliente por email.
 * Transforma a entidade em DTO para exposição externa.
 */
@Slf4j
@RequiredArgsConstructor
public class GetClientByEmailQueryHandler implements IQueryHandler<GetCustomerByEmailQuery, CustomerDto> {

    private final ClientRepository repository;

    @Override
    public Mono<CustomerDto> handle(GetCustomerByEmailQuery query) {
        log.debug("Handling GetClientByEmailQuery for email: {}", query.email());

        return repository.findByEmail(query.email())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com email: " + query.email())))
                .map(client -> new CustomerDto(
                        client.getId().getValue(),
                        client.getEmail(),
                        client.getRegistrationDate(),
                        client.getEmailUpdatedDate(),
                        client.getPasswordUpdatedDate()
                ))
                .doOnSuccess(dto -> log.debug("Found client with email: {}", dto.email()))
                .doOnError(error -> log.error("Error finding client: {}", error.getMessage()));
    }
} 