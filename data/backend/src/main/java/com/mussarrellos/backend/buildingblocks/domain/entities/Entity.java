package com.mussarrellos.backend.buildingblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity<TId> implements IEntity {

    private List<IDomainEvent> domainEvents;

    protected abstract TId getId();

    @Override
    public List<IDomainEvent> getDomainEvents() {
        return domainEvents == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(domainEvents);
    }

    @Override
    public void clearDomainEvents() {
        if (domainEvents != null) domainEvents.clear();
    }

    protected void addDomainEvent(IDomainEvent domainEvent) {
        if (domainEvents == null) domainEvents = new ArrayList<>();
        this.domainEvents.add(domainEvent);
    }

    protected void checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}
