package com.mussarrellos.backend.modules.product.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import lombok.RequiredArgsConstructor;

/**
 * Regra de negócio que verifica se o nome e a descrição de um produto são diferentes.
 * O nome e a descrição não devem ser idênticos para garantir melhor SEO e informações mais completas.
 */
@RequiredArgsConstructor
public class NameAndDescriptionMustNotBeTheSameRule implements IBusinessRule {

    private final String name;
    private final String description;

    @Override
    public boolean isBroken() {
        return name.equals(description);
    }

    @Override
    public String getMessage() {
        return "Name and description should not be the same";
    }
}
