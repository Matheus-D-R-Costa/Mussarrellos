package com.mussarrellos.backend.buildingblocks.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Interface para todos os eventos de domínio.
 * Define o contrato básico que todos os eventos de domínio devem implementar.
 */
public interface IDomainEvent {
    
    /**
     * Obtém o ID único do evento.
     *
     * @return O ID do evento
     */
    UUID getId();
    
    /**
     * Obtém o momento em que o evento ocorreu.
     *
     * @return Timestamp do evento
     */
    Instant getOccurredOn();
    
    /**
     * Obtém a versão do evento.
     * Útil para evolução de eventos e compatibilidade entre versões.
     *
     * @return Versão do evento
     */
    int getVersion();
    
    /**
     * Implementação do padrão Visitor.
     * Permite que o evento seja "visitado" por um visitor para processamento polimórfico.
     *
     * @param visitor O visitor que processará o evento
     */
    void accept(IDomainEventVisitor visitor);
} 