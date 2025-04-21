package com.mussarrellos.backend.modules.client.domain.entity.types;

import com.mussarrellos.backend.buildingblocks.TypedIdValueBase;

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
