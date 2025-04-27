package com.mussarrellos.backend.buildingblocks.application.outbox;

import reactor.core.publisher.Mono;

public interface Outbox {

    void add(OutboxMessage message);

    Mono<Void> save();

} 