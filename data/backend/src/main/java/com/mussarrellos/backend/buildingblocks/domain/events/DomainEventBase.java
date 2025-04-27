package com.mussarrellos.backend.buildingblocks.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Getter
public abstract class DomainEventBase implements IDomainEvent {

    private final UUID id;

    private final Instant occurredOn;

    private final int version;

    protected DomainEventBase() {
        this(UUID.randomUUID(), Instant.now(), 1);
    }

    protected DomainEventBase(UUID id, Instant occurredOn, int version) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.version = version;
    }

} 