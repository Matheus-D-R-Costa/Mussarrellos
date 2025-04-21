package com.mussarrellos.backend.buildingblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe base abstrata para todas as entidades de domínio.
 * Fornece implementação para gerenciamento de eventos de domínio e verificação de regras de negócio.
 * 
 * @param <TId> O tipo do identificador da entidade
 */
public abstract class Entity<TId> implements IEntity {
    private List<IDomainEvent> domainEvents;

    /**
     * Obtém o identificador único desta entidade.
     * 
     * @return O identificador da entidade
     */
    protected abstract TId getId();

    @Override
    public List<IDomainEvent> getDomainEvents() {
        return domainEvents == null ? Collections.emptyList() : Collections.unmodifiableList(domainEvents);
    }

    @Override
    public void clearDomainEvents() {
        if (domainEvents != null) {
            domainEvents.clear();
        }
    }

    /**
     * Adiciona um evento de domínio à lista de eventos pendentes.
     * 
     * @param domainEvent O evento de domínio a ser adicionado
     */
    protected void addDomainEvent(IDomainEvent domainEvent) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        
        this.domainEvents.add(domainEvent);
    }

    /**
     * Verifica uma regra de negócio e lança exceção se a regra for violada.
     * 
     * @param rule A regra de negócio a ser verificada
     * @throws BusinessRuleValidationException Se a regra for violada
     */
    protected void checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}
