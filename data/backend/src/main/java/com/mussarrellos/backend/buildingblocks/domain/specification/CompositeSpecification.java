package com.mussarrellos.backend.buildingblocks.domain.specification;

/**
 * Implementação base para especificações compostas.
 * Fornece uma classe base abstrata para implementações concretas do padrão Specification.
 *
 * @param <T> O tipo do objeto a ser avaliado pela especificação
 */
public abstract class CompositeSpecification<T> implements Specification<T> {
    
    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }
    
    @Override
    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }
    
    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }
    
    /**
     * Especificação que representa a conjunção (AND) de duas especificações.
     */
    private static class AndSpecification<T> extends CompositeSpecification<T> {
        private final Specification<T> left;
        private final Specification<T> right;
        
        public AndSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean isSatisfiedBy(T candidate) {
            return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
        }
    }
    
    /**
     * Especificação que representa a disjunção (OR) de duas especificações.
     */
    private static class OrSpecification<T> extends CompositeSpecification<T> {
        private final Specification<T> left;
        private final Specification<T> right;
        
        public OrSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean isSatisfiedBy(T candidate) {
            return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate);
        }
    }
    
    /**
     * Especificação que representa a negação (NOT) de uma especificação.
     */
    private static class NotSpecification<T> extends CompositeSpecification<T> {
        private final Specification<T> spec;
        
        public NotSpecification(Specification<T> spec) {
            this.spec = spec;
        }
        
        @Override
        public boolean isSatisfiedBy(T candidate) {
            return !spec.isSatisfiedBy(candidate);
        }
    }
} 