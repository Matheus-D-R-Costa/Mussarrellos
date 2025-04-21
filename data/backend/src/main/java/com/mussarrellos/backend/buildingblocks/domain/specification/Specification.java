package com.mussarrellos.backend.buildingblocks.domain.specification;

/**
 * Interface base para o padrão Specification.
 * Permite a definição de regras de negócio reutilizáveis e combináveis.
 *
 * @param <T> O tipo do objeto a ser avaliado pela especificação
 */
public interface Specification<T> {
    
    /**
     * Verifica se o candidato atende a esta especificação.
     *
     * @param candidate O objeto a ser avaliado
     * @return true se o objeto atende à especificação, false caso contrário
     */
    boolean isSatisfiedBy(T candidate);
    
    /**
     * Combina esta especificação com outra usando o operador lógico AND.
     *
     * @param other A outra especificação a ser combinada
     * @return Uma nova especificação que é satisfeita apenas se ambas as especificações forem satisfeitas
     */
    default Specification<T> and(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
    }
    
    /**
     * Combina esta especificação com outra usando o operador lógico OR.
     *
     * @param other A outra especificação a ser combinada
     * @return Uma nova especificação que é satisfeita se pelo menos uma das especificações for satisfeita
     */
    default Specification<T> or(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
    }
    
    /**
     * Inverte o resultado desta especificação (operador NOT).
     *
     * @return Uma nova especificação que é satisfeita apenas se esta especificação não for satisfeita
     */
    default Specification<T> not() {
        return candidate -> !this.isSatisfiedBy(candidate);
    }
} 