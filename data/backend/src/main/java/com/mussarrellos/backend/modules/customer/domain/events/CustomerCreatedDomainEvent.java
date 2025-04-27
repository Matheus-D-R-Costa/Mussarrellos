package com.mussarrellos.backend.modules.customer.domain.events;

import com.mussarrellos.backend.buildingblocks.domain.events.DomainEventBase;
import lombok.Getter;

import java.util.UUID;


@Getter
public class CustomerCreatedDomainEvent extends DomainEventBase {

    private final UUID userId;
    private final String email;

    public CustomerCreatedDomainEvent(UUID userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }
} 