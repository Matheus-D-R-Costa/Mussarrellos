package com.mussarrellos.backend.modules.client.domain.events;

import com.mussarrellos.backend.buildingblocks.domain.IDomainEvent;
import com.mussarrellos.backend.buildingblocks.domain.IDomainEventVisitor;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de domínio gerado quando o email de um cliente é alterado.
 * Implementado como um record para garantir imutabilidade completa.
 *
 * @param id ID único do evento
 * @param occurredOn Momento em que o evento ocorreu
 * @param version Versão do evento para compatibilidade
 * @param clientId ID do cliente cujo email foi alterado
 * @param oldEmail Email anterior do cliente
 * @param newEmail Novo email do cliente
 */
public record ClientEmailChangedEvent(
        UUID id,
        Instant occurredOn,
        int version,
        UUID clientId,
        String oldEmail,
        String newEmail
) implements IDomainEvent {
    
    /**
     * Construtor simplificado que usa valores padrão para id, timestamp e versão.
     *
     * @param clientId ID do cliente
     * @param oldEmail Email anterior
     * @param newEmail Novo email
     * @return Um novo evento com valores padrão
     */
    public static ClientEmailChangedEvent create(UUID clientId, String oldEmail, String newEmail) {
        return new ClientEmailChangedEvent(
                UUID.randomUUID(),
                Instant.now(),
                1,
                clientId,
                oldEmail,
                newEmail
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