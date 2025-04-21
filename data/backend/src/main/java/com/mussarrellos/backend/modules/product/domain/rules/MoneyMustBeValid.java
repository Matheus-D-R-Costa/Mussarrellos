package com.mussarrellos.backend.modules.product.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import com.mussarrellos.backend.modules.product.domain.Money;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * Regra de negócio que verifica se o valor monetário é válido.
 * Produtos devem ter preço maior que zero e moeda válida para serem aceitos no sistema.
 */
@RequiredArgsConstructor
public class MoneyMustBeValid implements IBusinessRule {

    private final Money money;

    @Override
    public boolean isBroken() {
        if (money == null) return true;
        if (money.getAmount() == null) return true;
        if (money.getAmount().compareTo(BigDecimal.ZERO) <= 0) return true;
        return money.getCurrency() == null;
    }

    @Override
    public String getMessage() {
        return "Money must have a positive amount and valid currency";
    }
} 