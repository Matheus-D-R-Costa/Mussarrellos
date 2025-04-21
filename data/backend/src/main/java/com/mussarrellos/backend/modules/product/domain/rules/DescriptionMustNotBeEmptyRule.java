package com.mussarrellos.backend.modules.product.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;

/**
 * Regra de negócio que verifica se a descrição de um produto não está vazia.
 * Todo produto deve ter uma descrição adequada para informar o cliente.
 */
@RequiredArgsConstructor
public class DescriptionMustNotBeEmptyRule implements IBusinessRule {

    private final String description;

    private static final int MAX_DESCRIPTION_SIZE = 500;

    @Override
    public boolean isBroken() {
        return description == null
                || description.isBlank()
                || description.length() > MAX_DESCRIPTION_SIZE;
    }

    @Override
    public String getMessage() {
        return "Invalid product description";
    }
} 