package com.mussarrellos.backend.modules.product.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;

/**
 * Regra de negócio que verifica se o nome de um produto não está vazio.
 * Todo produto deve ter um nome para identificação no catálogo.
 */
@RequiredArgsConstructor
public class NameMustNotBeEmptyRule implements IBusinessRule {

    private final String name;

    @Override
    public boolean isBroken() {
        return name == null || name.isBlank();
    }

    @Override
    public String getMessage() {
        return "Product name cannot be empty";
    }
} 