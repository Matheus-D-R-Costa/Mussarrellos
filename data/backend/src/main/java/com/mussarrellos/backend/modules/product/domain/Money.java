package com.mussarrellos.backend.modules.product.domain;

import com.mussarrellos.backend.buildingblocks.IgnoreMember;
import com.mussarrellos.backend.buildingblocks.ValueObject;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * Objeto de valor que representa um valor monetário com uma moeda.
 * Imutável e seguro para operações financeiras.
 */
@Getter
public class Money extends ValueObject {

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.getDefault());

    private final BigDecimal amount;
    private final Currency currency;

    @IgnoreMember
    private final String formattedAmount;

    /**
     * Cria uma nova instância de Money com valor e moeda específicos.
     *
     * @param amount   O valor monetário
     * @param currency A moeda
     * @throws IllegalArgumentException Se o valor for nulo
     */
    public Money(final BigDecimal amount, final Currency currency) {
        validateConstructorArguments(amount, currency);
        this.amount = amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
        this.currency = currency;
        this.formattedAmount = currency.getSymbol() + amount.toPlainString();
    }

    /**
     * Cria uma nova instância de Money com valor específico e moeda padrão.
     *
     * @param amount O valor monetário
     * @throws IllegalArgumentException Se o valor for nulo
     */
    public Money(final BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    /**
     * Cria uma nova instância de Money com valor zero e moeda específica.
     *
     * @param currency A moeda
     * @return Nova instância de Money com valor zero
     */
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    /**
     * Cria uma nova instância de Money com valor zero e moeda padrão.
     *
     * @return Nova instância de Money com valor zero
     */
    public static Money zero() {
        return zero(DEFAULT_CURRENCY);
    }

    /**
     * Adiciona outro valor monetário a este.
     *
     * @param other O valor a ser adicionado
     * @return Um novo Money com o resultado da adição
     * @throws IllegalArgumentException Se as moedas forem incompatíveis
     */
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Subtrai outro valor monetário deste.
     *
     * @param other O valor a ser subtraído
     * @return Um novo Money com o resultado da subtração
     * @throws IllegalArgumentException Se as moedas forem incompatíveis
     */
    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplica este valor monetário por um fator.
     *
     * @param factor O fator de multiplicação
     * @return Um novo Money com o resultado da multiplicação
     */
    public Money multiply(final BigDecimal factor) {
        Objects.requireNonNull(factor, "Factor must not be null");
        return new Money(this.amount.multiply(factor), this.currency);
    }

    /**
     * Divide este valor monetário por um divisor.
     *
     * @param divisor O divisor
     * @return Um novo Money com o resultado da divisão
     * @throws IllegalArgumentException Se o divisor for zero
     */
    public Money divide(final BigDecimal divisor) {
        Objects.requireNonNull(divisor, "Divisor must not be null");
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(divisor, DEFAULT_SCALE, DEFAULT_ROUNDING), this.currency);
    }

    /**
     * Verifica se este valor monetário é maior que outro.
     *
     * @param other O valor a ser comparado
     * @return true se este valor for maior que o outro
     * @throws IllegalArgumentException Se as moedas forem incompatíveis
     */
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    /**
     * Verifica se este valor monetário é menor que outro.
     *
     * @param other O valor a ser comparado
     * @return true se este valor for menor que o outro
     * @throws IllegalArgumentException Se as moedas forem incompatíveis
     */
    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    /**
     * Verifica se este valor monetário é igual a outro.
     *
     * @param other O valor a ser comparado
     * @return true se este valor for igual ao outro
     * @throws IllegalArgumentException Se as moedas forem incompatíveis
     */
    public boolean isEqualTo(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    /**
     * Verifica se este valor monetário é positivo.
     *
     * @return true se o valor for maior que zero
     */
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verifica se este valor monetário é zero.
     *
     * @return true se o valor for igual a zero
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public String toString() {
        return formattedAmount;
    }

    private void validateConstructorArguments(BigDecimal amount, Currency currency) {
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(currency, "Currency must not be null");
    }

    private void validateSameCurrency(Money other) {
        Objects.requireNonNull(other, "Other money must not be null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    String.format("Currency mismatch: %s vs %s", this.currency, other.currency));
        }
    }
}
