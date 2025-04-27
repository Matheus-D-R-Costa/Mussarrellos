package com.mussarrellos.backend.buildingblocks.application.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageFactory {

    private final ObjectMapper objectMapper;

    public OutboxMessage createFrom(IDomainEvent domainEvent) {
        try {
            String eventTypeName = domainEvent.getClass().getName();
            String data = objectMapper.writeValueAsString(domainEvent);
            
            return new OutboxMessage(
                    domainEvent.getId(),
                    domainEvent.getOccurredOn(),
                    eventTypeName,
                    data
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar o evento de domínio", e);
        }
    }
} 