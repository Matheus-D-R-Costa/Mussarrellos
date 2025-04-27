package com.mussarrellos.backend.modules.customer.application.queries.handlers;

import com.mussarrellos.backend.buildingblocks.application.queries.QueryHandler;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;
import com.mussarrellos.backend.modules.customer.application.queries.GetCustomerByIdQuery;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class GetCustomerByIdQueryHandler implements QueryHandler<GetCustomerByIdQuery, CustomerDto> {

    private final CustomerRepository repository;

    @Override
    public Mono<CustomerDto> handle(GetCustomerByIdQuery query) {
        log.debug("Processando consulta GetClientByIdQuery para ID: {}", query.clientId());
        
        return repository.findById(new CustomerId(query.clientId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com ID: " + query.clientId())))
            .map(client -> new CustomerDto(
                client.getId().getValue(),
                client.getEmail(),
                client.getRegistrationDate(),
                client.getEmailUpdatedDate(),
                client.getPasswordUpdatedDate()
            ))
            .doOnSuccess(dto -> log.debug("Cliente encontrado com ID: {}", dto.id()))
            .doOnError(error -> log.error("Erro ao buscar cliente: {}", error.getMessage()));
    }
} 