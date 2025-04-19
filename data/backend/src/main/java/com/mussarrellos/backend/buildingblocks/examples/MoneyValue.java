package com.mussarrellos.backend.buildingblocks.examples;

import com.mussarrellos.backend.buildingblocks.IgnoreMember;
import com.mussarrellos.backend.buildingblocks.ValueObject;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Exemplo de Value Object representando um valor monetário.
 */
public class MoneyValue extends ValueObject {
    private final BigDecimal amount;
    private final Currency currency;
    
    @IgnoreMember
    private final String formattedValue; // Este campo será ignorado na comparação
    
    public MoneyValue(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        
        this.amount = amount;
        this.currency = currency;
        this.formattedValue = currency.getSymbol() + amount.toString();
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public String getFormattedValue() {
        return formattedValue;
    }
    
    /**
     * Adiciona um valor a este valor monetário.
     * @param value O valor a ser adicionado.
     * @return Um novo MoneyValue com o valor somado.
     */
    public MoneyValue add(MoneyValue value) {
        if (!this.currency.equals(value.currency)) {
            throw new IllegalArgumentException("Cannot add money values with different currencies");
        }
        
        return new MoneyValue(this.amount.add(value.amount), this.currency);
    }
    
    /**
     * Multiplica este valor monetário por um fator.
     * @param factor O fator de multiplicação.
     * @return Um novo MoneyValue com o valor multiplicado.
     */
    public MoneyValue multiply(BigDecimal factor) {
        return new MoneyValue(this.amount.multiply(factor), this.currency);
    }
    
    @Override
    public String toString() {
        return formattedValue;
    }
} 