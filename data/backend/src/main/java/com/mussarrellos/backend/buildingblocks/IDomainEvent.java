package com.mussarrellos.backend.buildingblocks;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface para eventos de domínio.
 * Equivalente Java da interface C# IDomainEvent que herda de INotification do MediatR.
 * Em Java com Spring, usamos ApplicationEvent ou interfaces personalizadas para o mesmo propósito.
 */
public interface IDomainEvent {
    /**
     * Identificador único do evento.
     * @return UUID do evento
     */
    UUID getId();

    /**
     * Data e hora em que o evento ocorreu.
     * @return LocalDateTime da ocorrência
     */
    LocalDateTime getOccurredOn();
} 