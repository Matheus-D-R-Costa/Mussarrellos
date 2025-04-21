package com.mussarrellos.backend.modules.product.domain.events;

import com.mussarrellos.backend.buildingblocks.DomainEventBase;
import lombok.Getter;

import java.util.UUID;

/**
 * Evento de domínio que representa a criação de um novo produto.
 */
@Getter
public class ProductCreatedDomainEvent extends DomainEventBase {

    private final UUID productId;
    private final String name;

    public ProductCreatedDomainEvent(UUID productId, String name) {
        super();
        this.productId = productId;
        this.name = name;
    }
} 