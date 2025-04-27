package com.mussarrellos.backend.buildingblocks.domain.entities.types;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class TypedIdValueBase {

    private final UUID value;

    protected TypedIdValueBase(UUID value) {
        if (value == null) throw new IllegalArgumentException("Identificador não pode ser nulo!");
        if (value.equals(new UUID(0, 0))) throw new IllegalArgumentException("Identificador não pode ser 'Empty...'");
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TypedIdValueBase other = (TypedIdValueBase) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
} 