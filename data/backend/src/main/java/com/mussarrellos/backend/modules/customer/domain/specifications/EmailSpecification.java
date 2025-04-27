package com.mussarrellos.backend.modules.customer.domain.specifications;

import com.mussarrellos.backend.buildingblocks.domain.specifications.CompositeSpecification;

import java.util.regex.Pattern;

public class EmailSpecification extends CompositeSpecification<String> {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    @Override
    public boolean isSatisfiedBy(String email) {
        return email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
    }

    public static EmailSpecification withTrustedDomain() {
        return new TrustedDomainEmailSpecification();
    }

    public static EmailSpecification nonDisposable() {
        return new NonDisposableEmailSpecification();
    }

    private static class TrustedDomainEmailSpecification extends EmailSpecification {
        private static final String[] TRUSTED_DOMAINS = {
                "gmail.com", "outlook.com", "hotmail.com", "yahoo.com", 
                "icloud.com", "protonmail.com", "aol.com", "msn.com"
        };
        
        @Override
        public boolean isSatisfiedBy(String email) {
            if (!super.isSatisfiedBy(email)) {
                return false;
            }
            
            String domain = email.substring(email.lastIndexOf('@') + 1).toLowerCase();
            
            for (String trustedDomain : TRUSTED_DOMAINS) {
                if (domain.equals(trustedDomain)) {
                    return true;
                }
            }
            
            return false;
        }
    }

    // Evoluir essas duas listas para um txt depois?
    private static class NonDisposableEmailSpecification extends EmailSpecification {
        private static final String[] DISPOSABLE_DOMAINS = {
                "tempmail.com", "guerrillamail.com", "10minutemail.com",
                "mailinator.com", "throwawaymail.com", "yopmail.com"
        };
        
        @Override
        public boolean isSatisfiedBy(String email) {
            if (!super.isSatisfiedBy(email)) {
                return false;
            }
            
            String domain = email.substring(email.lastIndexOf('@') + 1).toLowerCase();
            
            for (String disposableDomain : DISPOSABLE_DOMAINS) {
                if (domain.equals(disposableDomain)) {
                    return false;
                }
            }
            
            return true;
        }
    }
} 