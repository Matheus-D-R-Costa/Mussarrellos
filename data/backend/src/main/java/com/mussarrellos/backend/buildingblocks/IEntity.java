package com.mussarrellos.backend.buildingblocks;

import java.util.List;

/**
 * Interface base para todas as entidades que podem produzir eventos de domínio.
 * Define o contrato básico que todas as entidades devem implementar para suportar
 * o padrão de Domain Events.
 */
public interface IEntity {
    /**
     * Obtém a lista de eventos de domínio pendentes desta entidade.
     * 
     * @return Lista imutável de eventos de domínio
     */
    List<IDomainEvent> getDomainEvents();
    
    /**
     * Limpa todos os eventos de domínio pendentes desta entidade.
     * Normalmente chamado após a persistência e publicação dos eventos.
     */
    void clearDomainEvents();
} 