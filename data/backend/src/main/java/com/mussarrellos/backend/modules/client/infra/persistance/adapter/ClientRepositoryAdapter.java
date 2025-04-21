package com.mussarrellos.backend.modules.client.infra.persistance.adapter;

import com.mussarrellos.backend.buildingblocks.IDomainEvent;
import com.mussarrellos.backend.buildingblocks.application.outbox.Outbox;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessage;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessageFactory;
import com.mussarrellos.backend.modules.client.domain.entity.Client;
import com.mussarrellos.backend.modules.client.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.client.domain.repository.ClientRepository;
import com.mussarrellos.backend.modules.client.infra.persistance.mapper.ClientModelMapper;
import com.mussarrellos.backend.modules.client.infra.persistance.repository.ClientModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Implementação do repositório de cliente.
 * Adapta a interface do repositório do domínio para o gateway da infraestrutura.
 * 
 * Esta classe não precisa da anotação @Repository pois é instanciada explicitamente via @Bean
 * na classe UserConfig.
 */
@Slf4j
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepository {

    private final ClientModelRepository repository;
    private final Outbox outbox;
    private final OutboxMessageFactory outboxMessageFactory;
    private final ClientModelMapper mapper;

    @Override
    public Mono<UUID> save(Client client) {
        log.debug("Saving client: {}", client.getEmail());
        return repository.save(mapper.toModel(client))
                .flatMap(savedClient -> {
                    for (IDomainEvent domainEvent : client.getDomainEvents()) {
                        OutboxMessage outboxMessage = outboxMessageFactory.createFrom(domainEvent);
                        outbox.add(outboxMessage);
                    }

                    return outbox.save()
                            .thenReturn(savedClient.getId());
                })
                .doOnSuccess(id -> client.clearDomainEvents())
                .doOnError(e -> log.error("Error creating client", e));
    }

    @Override
    public Mono<Client> findById(ClientId id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain)
                .doOnError(e -> log.error("Error finding client by ID: {}", id.getValue(), e));
    }

    @Override
    public Mono<Client> findByEmail(String email) {
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