package com.mussarrellos.backend.buildingblocks;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos de domínio.
 * Equivalente ao dispatcher do MediatR no contexto do C#.
 * Usa o ApplicationEventPublisher do Spring para publicar eventos.
 */
@Component
@RequiredArgsConstructor
public class DomainEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publica um evento de domínio.
     * @param event Evento a ser publicado
     */
    public void publish(IDomainEvent event) {
        eventPublisher.publishEvent(event);
    }
} 