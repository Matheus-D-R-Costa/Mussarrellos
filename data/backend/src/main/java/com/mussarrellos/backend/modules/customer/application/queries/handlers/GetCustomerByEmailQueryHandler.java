package com.mussarrellos.backend.modules.customer.application.queries.handlers;

import com.mussarrellos.backend.buildingblocks.application.queries.IQueryHandler;
import com.mussarrellos.backend.modules.customer.application.dtos.CustomerDto;
import com.mussarrellos.backend.modules.customer.application.queries.GetCustomerByEmailQuery;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class GetCustomerByEmailQueryHandler implements IQueryHandler<GetCustomerByEmailQuery, CustomerDto> {

    private final CustomerRepository repository;

    @Override
    public Mono<CustomerDto> handle(GetCustomerByEmailQuery query) {
        log.debug("Processando consulta GetClientByEmailQuery para email: {}", query.email());

        return repository.findByEmail(query.email())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente não encontrado com email: " + query.email())))
                .map(client -> new CustomerDto(
                        client.getId().getValue(),
                        client.getEmail(),
                        client.getRegistrationDate(),
                        client.getEmailUpdatedDate(),
                        client.getPasswordUpdatedDate()
                ))
                .doOnSuccess(dto -> log.debug("Cliente encontrado com email: {}", dto.email()))
                .doOnError(error -> log.error("Erro ao buscar cliente: {}", error.getMessage()));
    }
} 