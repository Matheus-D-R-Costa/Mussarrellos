package com.mussarrellos.backend.buildingblocks;

import java.util.UUID;

/**
 * Classe base para identificadores fortemente tipados (Typed IDs).
 * Permite criar classes de ID específicas para cada entidade, proporcionando
 * type safety e evitando problemas de confusão entre diferentes tipos de IDs.
 */
public abstract class TypedIdValueBase {
    private final UUID value;

    /**
     * Construtor protegido para ser usado apenas por classes derivadas.
     * @param value O valor UUID que representa o ID
     * @throws IllegalArgumentException Se o valor for nulo ou um UUID vazio
     */
    protected TypedIdValueBase(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Id value cannot be null!");
        }
        
        if (value.equals(new UUID(0, 0))) { // Equivalente ao Guid.Empty em C#
            throw new IllegalArgumentException("Id value cannot be empty!");
        }

        this.value = value;
    }

    /**
     * Obtém o valor UUID deste ID.
     * @return O valor UUID
     */
    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
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