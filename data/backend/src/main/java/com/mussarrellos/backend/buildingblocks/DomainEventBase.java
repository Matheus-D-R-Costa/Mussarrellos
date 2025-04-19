package com.mussarrellos.backend.buildingblocks;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe base para eventos de domínio.
 * Fornece uma implementação padrão para IDomainEvent.
 */
@Getter
public abstract class DomainEventBase implements IDomainEvent {
    private final UUID id;
    private final LocalDateTime occurredOn;

    protected DomainEventBase() {
        this.id = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
    }
} 