package com.mussarrellos.backend.modules.client.domain.specification;

import com.mussarrellos.backend.buildingblocks.domain.specification.CompositeSpecification;

import java.util.regex.Pattern;
import java.util.function.Predicate;

/**
 * Especificação que valida se uma senha atende aos requisitos de segurança.
 * Implementa o padrão Specification para validação de senhas.
 */
public class PasswordSpecification extends CompositeSpecification<String> {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{6,}$"
    );
    
    @Override
    public boolean isSatisfiedBy(String password) {
        return password != null && !password.isBlank() && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Cria uma especificação que valida se a senha NÃO corresponde a uma senha existente.
     * 
     * @param existingPasswordPredicate Predicado que verifica se a senha é igual à existente
     * @return Especificação para verificar que a senha é diferente da existente
     */
    public static PasswordSpecification notMatchingExisting(Predicate<String> existingPasswordPredicate) {
        return new NotMatchingExistingPasswordSpecification(existingPasswordPredicate);
    }
    
    /**
     * Cria uma especificação que valida se a senha tem o tamanho mínimo especificado.
     * 
     * @param minLength Tamanho mínimo da senha
     * @return Especificação para validar tamanho mínimo da senha
     */
    public static PasswordSpecification withMinLength(int minLength) {
        return new MinLengthPasswordSpecification(minLength);
    }
    
    /**
     * Cria uma especificação que valida se a senha é complexa o suficiente.
     * 
     * @return Especificação para validar complexidade da senha
     */
    public static PasswordSpecification withComplexity() {
        return new ComplexPasswordSpecification();
    }
    
    /**
     * Especificação que valida se a senha NÃO corresponde a uma senha existente.
     */
    private static class NotMatchingExistingPasswordSpecification extends PasswordSpecification {
        private final Predicate<String> existingPasswordPredicate;
        
        public NotMatchingExistingPasswordSpecification(Predicate<String> existingPasswordPredicate) {
            this.existingPasswordPredicate = existingPasswordPredicate;
        }
        
        @Override
        public boolean isSatisfiedBy(String password) {
            if (!super.isSatisfiedBy(password)) {
                return false;
            }
            
            // Retorna true se a senha NÃO corresponde à senha existente
            return !existingPasswordPredicate.test(password);
        }
    }
    
    /**
     * Especificação que valida se a senha tem o tamanho mínimo.
     */
    private static class MinLengthPasswordSpecification extends PasswordSpecification {
        private final int minLength;
        
        public MinLengthPasswordSpecification(int minLength) {
            this.minLength = minLength;
        }
        
        @Override
        public boolean isSatisfiedBy(String password) {
            return password != null && password.length() >= minLength;
        }
    }
    
    /**
     * Especificação que valida se a senha é complexa o suficiente.
     */
    private static class ComplexPasswordSpecification extends PasswordSpecification {
        private static final Pattern HAS_UPPERCASE = Pattern.compile(".*[A-Z].*");
        private static final Pattern HAS_LOWERCASE = Pattern.compile(".*[a-z].*");
        private static final Pattern HAS_DIGIT = Pattern.compile(".*\\d.*");
        private static final Pattern HAS_SPECIAL = Pattern.compile(".*[^a-zA-Z0-9].*");
        
        @Override
        public boolean isSatisfiedBy(String password) {
            if (password == null || password.isBlank()) {
                return false;
            }
            
            return HAS_UPPERCASE.matcher(password).matches() &&
                   HAS_LOWERCASE.matcher(password).matches() &&
                   HAS_DIGIT.matcher(password).matches() &&
                   HAS_SPECIAL.matcher(password).matches();
        }
    }
} 