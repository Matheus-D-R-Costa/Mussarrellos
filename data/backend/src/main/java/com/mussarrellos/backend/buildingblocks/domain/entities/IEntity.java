package com.mussarrellos.backend.buildingblocks.domain.entities;

import com.mussarrellos.backend.buildingblocks.domain.events.IDomainEvent;

import java.util.List;

public interface IEntity {

    List<IDomainEvent> getDomainEvents();

    void clearDomainEvents();
} 