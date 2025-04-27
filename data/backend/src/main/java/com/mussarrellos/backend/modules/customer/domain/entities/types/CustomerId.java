package com.mussarrellos.backend.modules.customer.domain.entities.types;

import com.mussarrellos.backend.buildingblocks.domain.entities.types.TypedIdValueBase;

import java.util.UUID;

public class ClientId extends TypedIdValueBase {

    /**
     * Construtor protegido para ser usado apenas por classes derivadas.
     *
     * @param value O valor UUID que representa o ID
     * @throws IllegalArgumentException Se o valor for nulo ou um UUID vazio
     */
    public ClientId(UUID value) {
        super(value);
    }

}
