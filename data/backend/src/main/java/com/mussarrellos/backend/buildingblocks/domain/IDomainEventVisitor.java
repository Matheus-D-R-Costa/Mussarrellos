package com.mussarrellos.backend.buildingblocks.domain;

import com.mussarrellos.backend.modules.client.domain.events.ClientCreatedDomainEvent;
import com.mussarrellos.backend.modules.client.domain.events.ClientEmailChangedEvent;
import com.mussarrellos.backend.modules.client.domain.events.ClientPasswordChangedEvent;

/**
 * Interface que define o padrão Visitor para eventos de domínio.
 * Permite processar eventos de domínio de forma polimórfica sem usar instanceof ou casting.
 * Cada tipo de evento tem um método visit específico.
 */
public interface IDomainEventVisitor {
    
    /**
     * Visita um evento de criação de cliente.
     * 
     * @param event O evento a ser visitado
     */
    void visit(ClientCreatedDomainEvent event);
    
    /**
     * Visita um evento de alteração de email de cliente.
     * 
     * @param event O evento a ser visitado
     */
    void visit(ClientEmailChangedEvent event);
    
    /**
     * Visita um evento de alteração de senha de cliente.
     * 
     * @param event O evento a ser visitado
     */
    void visit(ClientPasswordChangedEvent event);
    
    /**
     * Método padrão para tratar eventos desconhecidos.
     * Implementações podem sobrescrever para fornecer comportamento específico.
     * 
     * @param event O evento desconhecido
     */
    default void visitUnknown(IDomainEvent event) {
        // Implementação padrão vazia
    }
} 