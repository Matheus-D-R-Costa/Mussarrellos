package com.mussarrellos.backend.modules.customer.domain.events.handlers;

import com.mussarrellos.backend.modules.customer.domain.events.CustomerCreatedDomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
// TODO: Refatorar todas os DomainEventHandlers para usar a interface do buildingblocks...
@Slf4j
@Component
public class CustomerCreatedDomainEventListener {

    @EventListener
    public void handle(CustomerCreatedDomainEvent event) {
        log.info("User created event received: User {} with ID {} was created at {}",
                event.getEmail(),
                event.getUserId(),
                event.getOccurredOn());
    }
} 