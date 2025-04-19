package com.mussarrellos.backend.buildingblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe base para todas as entidades de domínio.
 * Fornece funcionalidades para gerenciamento de eventos de domínio e validação de regras de negócio.
 */
public abstract class Entity {
    private List<IDomainEvent> domainEvents;

    /**
     * Eventos de domínio ocorridos nesta entidade.
     * @return Uma coleção imutável de eventos de domínio.
     */
    public List<IDomainEvent> getDomainEvents() {
        return domainEvents == null ? Collections.emptyList() : Collections.unmodifiableList(domainEvents);
    }

    /**
     * Limpa todos os eventos de domínio registrados.
     */
    public void clearDomainEvents() {
        if (domainEvents != null) {
            domainEvents.clear();
        }
    }

    /**
     * Adiciona um evento de domínio.
     * @param domainEvent Evento de domínio a ser adicionado.
     */
    protected void addDomainEvent(IDomainEvent domainEvent) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        
        this.domainEvents.add(domainEvent);
    }

    /**
     * Verifica se uma regra de negócio é violada.
     * @param rule Regra de negócio a ser verificada.
     * @throws BusinessRuleValidationException Se a regra for violada.
     */
    protected void checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}
