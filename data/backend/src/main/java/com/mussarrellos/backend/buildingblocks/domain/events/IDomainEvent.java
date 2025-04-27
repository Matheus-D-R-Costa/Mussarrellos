package com.mussarrellos.backend.buildingblocks.domain.events;

import java.time.Instant;
import java.util.UUID;

public interface IDomainEvent {

    UUID getId();

    Instant getOccurredOn();

    int getVersion();

} 