package com.mussarrellos.backend.modules.client.domain.events;

import com.mussarrellos.backend.buildingblocks.DomainEventBase;
import lombok.Getter;

import java.util.UUID;

/**
 * Evento de domínio que representa a criação de um novo cliente.
 */
@Getter
public class ClientCreatedDomainEvent extends DomainEventBase {

    private final UUID userId;
    private final String email;

    public ClientCreatedDomainEvent(UUID userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }
} 