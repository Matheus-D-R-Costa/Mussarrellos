package com.mussarrellos.backend.buildingblocks.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Classe base para todos os eventos de domínio.
 * Fornece implementação comum para propriedades de eventos como ID e timestamp.
 */
@Getter
public abstract class DomainEventBase implements IDomainEvent {
    
    /**
     * ID único do evento.
     */
    private final UUID id;
    
    /**
     * Timestamp em que o evento foi criado.
     */
    private final Instant occurredOn;
    
    /**
     * Versão do evento. Útil para evolução de eventos e compatibilidade entre versões.
     */
    private final int version;
    
    /**
     * Construtor padrão que inicializa o evento com valores padrão.
     * ID gerado automaticamente, timestamp atual e versão 1.
     */
    protected DomainEventBase() {
        this(UUID.randomUUID(), Instant.now(), 1);
    }
    
    /**
     * Construtor que permite especificar todos os atributos.
     *
     * @param id ID do evento
     * @param occurredOn Timestamp do evento
     * @param version Versão do evento
     */
    protected DomainEventBase(UUID id, Instant occurredOn, int version) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.version = version;
    }
    
    /**
     * Implementação do padrão Visitor.
     * Cada subclasse deve implementar este método para chamar o método
     * visitor adequado baseado em seu tipo concreto.
     *
     * @param visitor O visitor que processará o evento
     */
    @Override
    public abstract void accept(IDomainEventVisitor visitor);
} 