package com.mussarrellos.backend.modules.customer.infra.persistance.adapter;

import com.mussarrellos.backend.buildingblocks.application.outbox.Outbox;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessage;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessageFactory;
import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;
import com.mussarrellos.backend.modules.customer.domain.entities.Customer;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import com.mussarrellos.backend.modules.customer.infra.persistance.mapper.CustomerModelMapper;
import com.mussarrellos.backend.modules.customer.infra.persistance.repository.CustomerModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerModelRepository repository;
    private final Outbox outbox;
    private final OutboxMessageFactory outboxMessageFactory;
    private final CustomerModelMapper mapper;

    @Override
    public Mono<UUID> save(Customer customer) {
        log.debug("Saving client: {}", customer.getEmail());
        return repository.save(mapper.toModel(customer))
                .flatMap(savedClient -> {
                    for (IDomainEvent domainEvent : customer.getDomainEvents()) {
                        OutboxMessage outboxMessage = outboxMessageFactory.createFrom(domainEvent);
                        outbox.add(outboxMessage);
                    }

                    return outbox.save()
                            .thenReturn(savedClient.getId());
                })
                .doOnSuccess(id -> customer.clearDomainEvents())
                .doOnError(e -> log.error("Error creating client", e));
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain)
                .doOnError(e -> log.error("Error finding client by ID: {}", id.getValue(), e));
    }

    @Override
    public Mono<Customer> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> checkEmailUniqueness(String email) {
        return repository.existsByEmail(email)
                .map(exists -> !exists)
                .doOnError(e -> log.error("Error checking email uniqueness: {}", email, e));
    }
} 