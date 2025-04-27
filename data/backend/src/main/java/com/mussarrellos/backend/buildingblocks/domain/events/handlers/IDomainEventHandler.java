package com.mussarrellos.backend.buildingblocks.domain.events.handlers;

import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;
import reactor.core.publisher.Mono;

public interface IDomainEventHandler<T extends IDomainEvent> {

    Mono<Void> handle(T event);

}
