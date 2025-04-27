package com.mussarrellos.backend.modules.customer.domain.events;

import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;
import com.mussarrellos.backend.buildingblocks.domain.IDomainEventVisitor;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de domínio gerado quando a senha de um cliente é alterada.
 * Implementado como um record para garantir imutabilidade completa.
 * 
 * @param id ID único do evento
 * @param occurredOn Momento em que o evento ocorreu
 * @param version Versão do evento para compatibilidade
 * @param clientId ID do cliente cuja senha foi alterada
 */
public record ClientPasswordChangedEvent(
    UUID id,
    Instant occurredOn,
    int version,
    UUID clientId
) implements IDomainEvent {
    
    /**
     * Construtor simplificado que usa valores padrão para id, timestamp e versão.
     * 
     * @param clientId ID do cliente
     * @return Um novo evento com valores padrão
     */
    public static ClientPasswordChangedEvent create(UUID clientId) {
        return new ClientPasswordChangedEvent(
            UUID.randomUUID(),
            Instant.now(),
            1,
            clientId
        );
    }
    
    /**
     * Implementação do padrão Visitor.
     * Chama o método correspondente no visitor.
     * 
     * @param visitor O visitor que processará o evento
     */
    @Override
    public void accept(IDomainEventVisitor visitor) {
        visitor.visit(this);
    }
} 