package com.mussarrellos.backend.modules.client.domain.specification;

import com.mussarrellos.backend.buildingblocks.domain.specification.CompositeSpecification;

import java.util.regex.Pattern;

/**
 * Especificação que valida se um endereço de email tem formato válido.
 * Implementa o padrão Specification para regras de validação de email.
 */
public class EmailSpecification extends CompositeSpecification<String> {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    @Override
    public boolean isSatisfiedBy(String email) {
        return email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Cria uma nova especificação que verifica se o domínio do email é de um provedor confiável.
     * 
     * @return Uma especificação para domínios confiáveis
     */
    public static EmailSpecification withTrustedDomain() {
        return new TrustedDomainEmailSpecification();
    }
    
    /**
     * Cria uma nova especificação que verifica se o email não é descartável.
     * 
     * @return Uma especificação para emails não-descartáveis
     */
    public static EmailSpecification nonDisposable() {
        return new NonDisposableEmailSpecification();
    }
    
    /**
     * Especificação que valida se um email pertence a um domínio confiável.
     */
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
    
    /**
     * Especificação que valida se um email não é de um serviço descartável.
     */
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