package com.mussarrellos.backend.modules.customer.domain.entities.types;

import com.mussarrellos.backend.buildingblocks.domain.entities.types.TypedIdValueBase;

import java.util.UUID;

public class CustomerId extends TypedIdValueBase {

    public CustomerId(UUID value) {
        super(value);
    }

}
