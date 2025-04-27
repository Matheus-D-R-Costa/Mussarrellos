package com.mussarrellos.backend.buildingblocks.domain.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(IDomainEvent event) {
        eventPublisher.publishEvent(event);
    }

} 