package com.mussarrellos.backend.modules.customer.domain.events;

import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;

import java.time.Instant;
import java.util.UUID;


public record ClientEmailChangedEvent(
        UUID id,
        Instant occurredOn,
        int version,
        UUID clientId,
        String oldEmail,
        String newEmail
) implements IDomainEvent {

    public static ClientEmailChangedEvent create(UUID clientId, String oldEmail, String newEmail) {
        return new ClientEmailChangedEvent(
                UUID.randomUUID(),
                Instant.now(),
                1,
                clientId,
                oldEmail,
                newEmail
        );
    }

} 