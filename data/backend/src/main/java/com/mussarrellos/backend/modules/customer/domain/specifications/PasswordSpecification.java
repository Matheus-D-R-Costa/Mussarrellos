package com.mussarrellos.backend.modules.customer.domain.specifications;

import com.mussarrellos.backend.buildingblocks.domain.specifications.CompositeSpecification;

import java.util.regex.Pattern;
import java.util.function.Predicate;

public class PasswordSpecification extends CompositeSpecification<String> {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{6,}$"
    );
    
    @Override
    public boolean isSatisfiedBy(String password) {
        return password != null && !password.isBlank() && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static PasswordSpecification notMatchingExisting(Predicate<String> existingPasswordPredicate) {
        return new NotMatchingExistingPasswordSpecification(existingPasswordPredicate);
    }

    public static PasswordSpecification withMinLength(int minLength) {
        return new MinLengthPasswordSpecification(minLength);
    }

    public static PasswordSpecification withComplexity() {
        return new ComplexPasswordSpecification();
    }

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