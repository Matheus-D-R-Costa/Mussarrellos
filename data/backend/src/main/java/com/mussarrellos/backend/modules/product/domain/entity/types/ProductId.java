package com.mussarrellos.backend.modules.product.domain.entity.types;

import com.mussarrellos.backend.buildingblocks.TypedIdValueBase;

import java.util.UUID;

/**
 * ID tipado para a entidade Product.
 * Fornece type safety e evita confusão entre diferentes tipos de IDs.
 */
public class ProductId extends TypedIdValueBase {

    /**
     * Construtor para criar uma nova instância de ProductId.
     *
     * @param value O valor UUID que representa o ID
     * @throws IllegalArgumentException Se o valor for nulo ou um UUID vazio
     */
    public ProductId(UUID value) {
        super(value);
    }
}
