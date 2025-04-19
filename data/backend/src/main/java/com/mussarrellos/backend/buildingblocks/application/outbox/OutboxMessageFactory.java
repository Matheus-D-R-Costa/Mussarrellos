package com.mussarrellos.backend.buildingblocks.application.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mussarrellos.backend.buildingblocks.IDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Fábrica para criar mensagens de outbox a partir de eventos de domínio.
 * Facilita a conversão de eventos em mensagens serializadas para armazenamento.
 */
@Component
@RequiredArgsConstructor
public class OutboxMessageFactory {
    private final ObjectMapper objectMapper;

    /**
     * Cria uma mensagem de outbox a partir de um evento de domínio.
     *
     * @param domainEvent O evento de domínio a ser convertido
     * @return A mensagem de outbox correspondente
     * @throws RuntimeException Se ocorrer um erro na serialização do evento
     */
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
            throw new RuntimeException("Error serializing domain event", e);
        }
    }
} 